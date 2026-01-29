package server;

import DTO.GameStateDTO;
import DTO.PlayerDTO;
import DTO.TileDTO;
import actions.Action;
import dataStructures.hashtable.HashTable;
import dataStructures.stack.Stack;
import model.Board;
import model.CardType;
import model.Player;
import model.TopK;
import tiles.TileType;
import tiles.block.*;
import tiles.data.ChanceData;
import tiles.data.PropertyData;
import tiles.data.TAXData;
import tiles.structures.Home;
import tiles.structures.Hotel;

import java.util.Random;

/**
 * Server-side "Single Source of Truth" for the game.
 *
 * This is a simplified but working implementation based on the PDF requirements:
 * - 4 players
 * - Turn management
 * - Dice roll (server-side)
 * - Move on a circular board (CircularLinkedList)
 * - Property buy + rent + tax + chance + jail
 * - Undo/Redo for last reversible action(s) (stack of Action)
 *
 * NOTE: Trade/Build/Mortgage and advanced reports can be extended on top of this base.
 */
public class GameState {

    // Check if player owns all properties of the same color group



    public enum Phase {
        TURN_START,
        ROLL,
        RESOLVE,
        DECISION,
        TURN_END,
        FINISHED
    }

    private static final int MAX_PLAYERS = 4;
    private static final int INITIAL_MONEY = 1500;

    private final Random random = new Random();
    private final Board board = new Board();
    private final HashTable<Integer, Player> players = new HashTable<>();

    private final Stack<Action> undoStack = new Stack<>();
    private final Stack<Action> redoStack = new Stack<>();

    private int currentPlayerId = 1;
    private int turnCounter = 0;
    private Phase phase = Phase.TURN_START;

    // When the current player lands on an unowned property, server waits for BUY_PROPERTY or END_TURN.
    private TileProperty pendingBuy;

    private TopK topK;

    public synchronized boolean isReady() {
        return players.size() == MAX_PLAYERS;
    }

    public synchronized void addPlayer(int id, String name) {
        if (players.size() >= MAX_PLAYERS) {
            return;
        }
        Player p = new Player(name, id);
        p.setBalance(INITIAL_MONEY);
        // Start at GO
        p.setCurrentNode(board.getTiles().getHead());
        p.setPosition(0);
        p.setStatus(Player.Status.ACTIVE);
        players.put(id, p);

        if (players.size() == MAX_PLAYERS) {
            // First active player is player 1 by default (can be randomized later)
            currentPlayerId = 1;
            phase = Phase.TURN_START;
            Player[] arr = new Player[MAX_PLAYERS];
            for (int pid = 1; pid <= MAX_PLAYERS; pid++) {
                arr[pid - 1] = players.get(pid);
            }
            topK = new TopK(arr);
        }
    }

    public synchronized Player getPlayer(int id) {
        return players.get(id);
    }

    public Board getBoard() {
        return board;
    }

    public synchronized int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public synchronized Phase getPhase() {
        return phase;
    }

    public synchronized int rollDice() {
        // 2..12
        int d1 = 1 + random.nextInt(6);
        int d2 = 1 + random.nextInt(6);
        return d1 + d2;
    }

    /**
     * Called by server loop periodically (kept for compatibility with your server skeleton).
     * In this simplified implementation, nothing is queued here.
     */
    public void process() {
        // no-op for now
    }

    /* ========================= VALIDATION HELPERS ========================= */

    public synchronized boolean canAct(int playerId) {
        if (!isReady()) return false;
        if (phase == Phase.FINISHED) return false;

        Player p = players.get(playerId);
        if (p == null) return false;
        if (p.getStatus() == Player.Status.BANKRUPT) return false;

        return playerId == currentPlayerId;
    }

    /* ========================= GAME COMMANDS ========================= */

    public synchronized String cmdRollDice(int playerId) {
        if (!canAct(playerId)) return "Not your turn";
        if (phase != Phase.TURN_START && phase != Phase.ROLL) return "Cannot roll now";

        phase = Phase.ROLL;
        Player p = players.get(playerId);
        if (p.isInJail()) {
            // simplified jail: player skips 2 turns. We just decrease jailTurns.
            p.setJailTurns(p.getJailTurns() + 1);
            if (p.getJailTurns() >= 2) {
                p.setInJail(false);
                p.setJailTurns(0);
            }
            phase = Phase.TURN_END;
            return p.getName() + " is in jail and misses this turn";
        }

        int roll = rollDice();

        // Execute as an Action for undo/redo
        Action action = new server.actions.MoveAndResolveAction(this, p, roll);
        action.execute();
        undoStack.push(action);
        redoStack.clear();

        if (pendingBuy != null) {
            phase = Phase.DECISION;
            return p.getName() + " rolled " + roll + " and can buy this property";
        }

        phase = Phase.TURN_END;
        return p.getName() + " rolled " + roll;
    }

    public synchronized String cmdBuyProperty(int playerId) {
        if (!canAct(playerId)) return "Not your turn";
        if (phase != Phase.DECISION) return "No property to buy";
        if (pendingBuy == null) return "No property to buy";

        Player p = players.get(playerId);
        PropertyData data = (PropertyData) pendingBuy.getData();
        if (data.getOwner() != null) {
            pendingBuy = null;
            phase = Phase.TURN_END;
            return "Property already owned";
        }
        if (p.getBalance() < data.getPurchasePrice()) {
            return "Not enough money";
        }

        Action action = new server.actions.BuyPropertyAction(p, pendingBuy);
        action.execute();
        undoStack.push(action);
        redoStack.clear();

        pendingBuy = null;
        phase = Phase.TURN_END;
        return p.getName() + " bought the property";
    }

    
    public synchronized String cmdBuild(int playerId, String kind) {
        if (!canAct(playerId)) return "Not your turn";
//        if (finished) return "Game finished";

        Player p = players.get(playerId);
        Tile tile = p.getCurrentNode().getTile();
        if (!(tile instanceof TileProperty propTile)) {
            return "You are not on a property";
        }

        PropertyData pd = (PropertyData) propTile.getData();
        if (pd.getOwner() == null || pd.getOwner().getId() != playerId) {
            return "You do not own this property";
        }
        if (pd.isMortgaged()) {
            return "Property is mortgaged";
        }

        // Monopoly rule: you can build only if you own the full color group.
        if (pd.getColorGroup() != null) {
            tiles.ColorGroupProperty grp = board.getColorGroupProperty(pd.getColorGroup().ordinal());
            if (grp != null && !grp.propertyColorCheck(p)) {
                return "You must own all properties of this color group to build";
            }
        }

        String k = (kind == null) ? "HOUSE" : kind.toUpperCase();

        if ("HOUSE".equals(k)) {
            if (pd.isHasHotel()) return "Property already has a hotel";
            if (pd.getHouseCount() >= 4) return "Maximum houses reached (build hotel)";
            Home home = new Home(p);
            if (p.getBalance() < home.getPrice()) return "Not enough money";
            Action action = new server.actions.BuildHouseAction(p, propTile, home.getPrice());
            action.execute();
            undoStack.push(action);
            redoStack.clear();
            return "Built a house on " + pd.getName();
        }

        if ("HOTEL".equals(k)) {
            if (pd.isHasHotel()) return "Property already has a hotel";
            if (pd.getHouseCount() < 4) return "Need 4 houses before hotel";
            Hotel hotel = new Hotel(p);
            if (p.getBalance() < hotel.getPrice()) return "Not enough money";
            Action action = new server.actions.BuildHotelAction(p, propTile, hotel.getPrice());
            action.execute();
            undoStack.push(action);
            redoStack.clear();
            return "Built a hotel on " + pd.getName();
        }

        return "Unknown build kind";
    }

public synchronized String cmdEndTurn(int playerId) {
        if (!canAct(playerId)) return "Not your turn";
        if (phase != Phase.TURN_END && phase != Phase.DECISION) return "Cannot end now";

        pendingBuy = null;

        // next active player
        int next = currentPlayerId;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            next = (next % MAX_PLAYERS) + 1; // 1..4
            Player p = players.get(next);
            if (p != null && p.getStatus() != Player.Status.BANKRUPT) {
                currentPlayerId = next;
                break;
            }
        }

        turnCounter++;
        phase = Phase.TURN_START;
        if (topK != null) topK.update();

        // finish check
        int activeCount = 0;
        int lastActiveId = -1;
        for (int pid = 1; pid <= MAX_PLAYERS; pid++) {
            Player p = players.get(pid);
            if (p != null && p.getStatus() != Player.Status.BANKRUPT) {
                activeCount++;
                lastActiveId = p.getId();
            }
        }
        if (activeCount <= 1 && lastActiveId != -1) {
            phase = Phase.FINISHED;
            return "Game finished. Winner: Player " + lastActiveId;
        }

        return "Turn changed to Player " + currentPlayerId;
    }

    public synchronized String cmdUndo(int playerId) {
        if (!canAct(playerId)) return "Not your turn";
        if (undoStack.isEmpty()) return "Nothing to undo";
        // only allowed at TURN_END or DECISION (matches PDF idea)
        if (phase != Phase.TURN_END && phase != Phase.DECISION) return "Undo not allowed now";

        Action a = undoStack.pop();
        a.undo();
        redoStack.push(a);
        // If we undid a move that created a pending buy, clear it
        pendingBuy = null;
        // after undo, we allow rolling again
        phase = Phase.TURN_START;
        return "Undo done";
    }

    public synchronized String cmdRedo(int playerId) {
        if (!canAct(playerId)) return "Not your turn";
        if (redoStack.isEmpty()) return "Nothing to redo";
        if (phase != Phase.TURN_START && phase != Phase.TURN_END) return "Redo not allowed now";

        Action a = redoStack.pop();
        a.execute();
        undoStack.push(a);
        phase = Phase.TURN_END;
        return "Redo done";
    }

    /* ========================= INTERNAL: RESOLVE TILE ========================= */

    public synchronized void resolveAfterMove(Player player, boolean passedGo) {
        if (passedGo) {
            // GO reward is already handled by the GO tile data in your code;
            // to keep consistent, we just add 200 here.
            player.addBalance(200);
        }

        Tile tile = player.getCurrentNode().getTile();
        TileType type = tile.getTileType();

        if (type == TileType.TAX) {
            TAXData taxData = (TAXData) tile.getData();
            int tax = taxData.getTaxAmount();
            payToBank(player, tax);
        } else if (type == TileType.PROPERTY) {
            TileProperty property = (TileProperty) tile;
            PropertyData data = (PropertyData) property.getData();
            if (data.getOwner() == null) {
                pendingBuy = property;
            } else if (data.getOwner() != player && !data.isMortgaged()) {
                int rent = calculateRent(data);
                payRent(player, data.getOwner(), rent);
            }
        } else if (type == TileType.CARD) {
            ChanceData cd = (ChanceData) tile.getData();
            CardType card = cd.runCard(player);
            // simplified effects
            switch (card) {
                case PAY_MONEY -> payToBank(player, 200);
                case DARYAFT_MONEY -> player.addBalance(200);
                case JAIL -> {
                    player.setInJail(true);
                    player.setJailTurns(0);
                }
                default -> {
                    // other card types can be implemented later
                }
            }
        } else if (type == TileType.JAIL) {
            // landing on jail sends player to jail (simplified)
            player.setInJail(true);
            player.setJailTurns(0);
        }
    }

    private void payToBank(Player p, int amount) {
        if (p.getBalance() < amount) {
            bankrupt(p);
            return;
        }
        p.reduceBalance(amount);
    }

    private void payRent(Player from, Player to, int amount) {
        if (from.getBalance() < amount) {
            bankrupt(from);
            return;
        }
        from.reduceBalance(amount);
        to.addBalance(amount);
        to.addProfitRent(amount);
        // TODO: update Graph of interactions (required by PDF)
    }

    private void bankrupt(Player p) {
        p.setStatus(Player.Status.BANKRUPT);
        p.setBalance(0);
        // TODO: return properties to bank (required by PDF)
    }

    /* ========================= SNAPSHOT ========================= */

    
    private int calculateRent(PropertyData data) {
        if (data == null) return 0;
        int base = data.getBaseRent();
        Player owner = data.getOwner();
        if (owner == null) return base;
        if (data.isHasHotel()) {
            Hotel h = new Hotel(owner);
            return base + h.getRent();
        }
        int hc = data.getHouseCount();
        if (hc <= 0) return base;
        Home home = new Home(owner);
        return base + (hc * home.getRent());
    }

    public synchronized GameStateDTO snapshot() {
        GameStateDTO dto = new GameStateDTO();
        dto.turnCounter = turnCounter;
        dto.currentPlayerId = currentPlayerId;
        dto.phase = phase.name();
        dto.finished = (phase == Phase.FINISHED);

        PlayerDTO[] arr = new PlayerDTO[MAX_PLAYERS];
        for (int id = 1; id <= MAX_PLAYERS; id++) {
            Player p = players.get(id);
            if (p == null) {
                arr[id - 1] = new PlayerDTO(id, 0, 0);
            } else {
                arr[id - 1] = new PlayerDTO(p.getId(), p.getBalance(), p.getPosition());
            }
        }
        dto.players = arr;
        // Build board tiles for GUI (names + ownership + mortgage)
        int size = board.getTiles().getSize();
        TileDTO[] tilesArr = new TileDTO[size];
        dataStructures.linkedlist.Node node = board.getTiles().getHead();
        for (int i = 0; i < size; i++) {
            Tile t0 = (Tile) node.getTile();
            TileDTO td = new TileDTO();
            td.index = t0.getIndex();
            td.type = t0.getTileType().name();

            // default name
            td.name = td.type + " " + td.index;
            td.price = 0;
            td.rent = 0;
            td.ownerId = -1;
            td.mortgaged = false;
            td.houses = 0;
            td.hotels = 0;
            td.colorGroup = null;

            if (t0 instanceof TileProperty tp0) {
                PropertyData pd0 = (PropertyData) tp0.getData();
                String nm = pd0.getName();
                if (nm == null || nm.trim().isEmpty()) nm = "Property " + td.index;
                td.name = nm;
                td.colorGroup = (pd0.getColorGroup() == null) ? null : pd0.getColorGroup().name();
                td.price = pd0.getPurchasePrice();
                td.houses = pd0.getHouseCount();
                td.hotels = pd0.isHasHotel() ? 1 : 0;
                td.rent = calculateRent(pd0);
                td.ownerId = pd0.getOwnerId();
                td.mortgaged = pd0.isMortgaged();
            } else if (t0 instanceof TileGo) {
                td.name = "GO";
            } else if (t0 instanceof TileJail) {
                td.name = "JAIL";
            } else if (t0 instanceof TileTax) {
                td.name = "TAX";
            } else if (t0 instanceof TileChance) {
                td.name = "CHANCE";
            }
            tilesArr[i] = td;
            node = node.getNext();
        }
        dto.tiles = tilesArr;


        Player cur = players.get(currentPlayerId);
        if (cur != null && cur.getCurrentNode() != null) {
            dto.currentTileIndex = cur.getCurrentNode().getTile().getIndex();
            Tile t = cur.getCurrentNode().getTile();
            if (t instanceof TileProperty tp) {
                PropertyData pd = (PropertyData) tp.getData();
                dto.canBuyProperty = (phase == Phase.DECISION && pd.getOwner() == null && cur.getBalance() >= pd.getPurchasePrice());
            } else {
                dto.canBuyProperty = false;
            }
        }


        // Build availability for current player (UI helpers)
        dto.canBuildHouse = false;
        dto.canBuildHotel = false;
        Player curP = players.get(dto.currentPlayerId);
        if (curP != null) {
            Tile curTile = curP.getCurrentNode().getTile();
            if (curTile instanceof TileProperty tp) {
                PropertyData pd = (PropertyData) tp.getData();
                    boolean ownsGroup = true;
                    if (pd.getColorGroup() != null) {
                        tiles.ColorGroupProperty grp = board.getColorGroupProperty(pd.getColorGroup().ordinal());
                        if (grp != null) ownsGroup = grp.propertyColorCheck(curP);
                    }
                if (pd.getOwner() != null && pd.getOwner().getId() == dto.currentPlayerId && !pd.isMortgaged()) {
                    dto.canBuildHouse = (ownsGroup && !pd.isMortgaged() && !pd.isHasHotel() && pd.getHouseCount() < 4);
                    dto.canBuildHotel = (ownsGroup && !pd.isMortgaged() && !pd.isHasHotel() && pd.getHouseCount() >= 4);
                }
            }
        }

        dto.allowed = server.ServerRules.allowedCommandsFor(dto);
        return dto;
    }
}