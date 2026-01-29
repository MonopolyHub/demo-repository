package client;

import DTO.GameStateDTO;
import DTO.PlayerDTO;
import DTO.TileDTO;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * BoardPanel (Pro+ UI)
 * Adds:
 * 1) Type icons (GO/JAIL/CHANCE/TAX) - painted via small vector drawing (no image files)
 * 2) Corner styling + special backgrounds for corner tiles
 * 3) Houses/Hotel markers for properties (if available in TileDTO.houses/hotels)
 */
public class BoardPanel extends JPanel {
    // index 1..4
    private static final Color[] tokenColors = new Color[5];


    private static final int TILE_COUNT = 40;
    private static final int GRID = 11;

    private final TileCell[] cells = new TileCell[TILE_COUNT];

    private final Border normalBorder = BorderFactory.createLineBorder(new Color(0, 0, 0, 35), 1);
    private final Border highlightBorder = BorderFactory.createLineBorder(new Color(0, 120, 215), 3);

    public BoardPanel() {
        super(new GridLayout(GRID, GRID, 2, 2));
        initDefaultTokenColors();
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        buildGrid();
    }

    private void buildGrid() {
        for (int row = 0; row < GRID; row++) {
            for (int col = 0; col < GRID; col++) {
                Integer idx = tileIndexAt(row, col);
                if (idx == null) {
                    add(createCenterCell());
                } else {
                    TileCell cell = new TileCell(idx);
                    cell.setBorder(normalBorder);
                    cells[idx] = cell;
                    add(cell);
                }
            }
        }
    }

    private JPanel createCenterCell() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1));
        p.setOpaque(false);

        JLabel center = new JLabel("MONOPOLY HUB", SwingConstants.CENTER);
        center.setFont(center.getFont().deriveFont(Font.BOLD, 18f));
        center.setForeground(new Color(0, 0, 0, 120));
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    public void updateState(GameStateDTO state, int localPlayerId) {
        for (int i = 0; i < TILE_COUNT; i++) {
            if (cells[i] != null) {
                cells[i].setBorder(normalBorder);
                cells[i].reset(localPlayerId);
            }
        }
        if (state == null) return;

        if (state.tiles != null) {
            for (TileDTO t : state.tiles) {
                if (t == null) continue;
                int idx = t.index;
                if (idx >= 0 && idx < TILE_COUNT && cells[idx] != null) {
                    cells[idx].setTile(t);
                }
            }
        }

        if (state.players != null) {
            for (PlayerDTO p : state.players) {
                if (p == null) continue;
                int pos = p.getPosition();
                if (pos >= 0 && pos < TILE_COUNT && cells[pos] != null) {
                    cells[pos].addPlayer(p);
                }
            }
        }

        // Highlight current player's tile
        if (state.players != null) {
            for (PlayerDTO p : state.players) {
                if (p != null && p.getId() == state.currentPlayerId) {
                    int pos = p.getPosition();
                    if (pos >= 0 && pos < TILE_COUNT && cells[pos] != null) {
                        cells[pos].setBorder(highlightBorder);
                    }
                    break;
                }
            }
        }

        revalidate();
        repaint();
    }

    private Integer tileIndexAt(int row, int col) {
        if (row == 10) {
            return 10 - col; // bottom row 0..10
        }
        if (col == 0) {
            if (row >= 0 && row <= 9) return 10 + (10 - row); // left 11..20
        }
        if (row == 0) {
            if (col >= 1 && col <= 10) return 20 + col; // top 21..30
        }
        if (col == 10) {
            if (row >= 1 && row <= 9) return 30 + row; // right 31..39
        }
        return null;
    }

    private class TileCell extends JPanel {

        private final int index;

        private final JPanel stripe = new JPanel();
        private final JLabel name = new JLabel("", SwingConstants.CENTER);
        private final JLabel info = new JLabel("", SwingConstants.CENTER);
        private final JLabel owner = new JLabel("", SwingConstants.CENTER);
        private final JLabel mortgage = new JLabel("", SwingConstants.CENTER);

        private final HousesPanel housesPanel = new HousesPanel();
        private final JLabel tokens = new JLabel("", SwingConstants.CENTER);

        private final TypeIcon typeIcon = new TypeIcon();

        private TileDTO tile;
        private PlayerDTO[] playersOnThis = new PlayerDTO[0];
        private int localPlayerId = -1;

        public TileCell(int index) {
            super(new BorderLayout(2, 2));
            this.index = index;
            setOpaque(true);

            stripe.setPreferredSize(new Dimension(10, 6));
            stripe.setOpaque(true);

            boolean isCorner = (index % 10 == 0);
            float titleSize = isCorner ? 12f : 10.5f;
            float infoSize = isCorner ? 11f : 10f;

            name.setFont(name.getFont().deriveFont(Font.BOLD, titleSize));
            info.setFont(info.getFont().deriveFont(Font.PLAIN, infoSize));
            owner.setFont(owner.getFont().deriveFont(Font.PLAIN, 10f));
            mortgage.setFont(mortgage.getFont().deriveFont(Font.BOLD, 10f));
            tokens.setFont(tokens.getFont().deriveFont(Font.PLAIN, 10f));

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(stripe, BorderLayout.NORTH);

            JPanel titleRow = new JPanel(new BorderLayout(2, 2));
            titleRow.setOpaque(false);
            JLabel iconLabel = new JLabel(typeIcon);
            iconLabel.setHorizontalAlignment(SwingConstants.LEFT);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
            titleRow.add(iconLabel, BorderLayout.WEST);
            titleRow.add(name, BorderLayout.CENTER);
            top.add(titleRow, BorderLayout.CENTER);

            JPanel middle = new JPanel(new BorderLayout());
            middle.setOpaque(false);
            JPanel infoWrap = new JPanel(new GridLayout(2, 1));
            infoWrap.setOpaque(false);
            infoWrap.add(info);

            JPanel ownerRow = new JPanel(new GridLayout(1, 2, 2, 2));
            ownerRow.setOpaque(false);
            ownerRow.add(owner);
            ownerRow.add(mortgage);

            infoWrap.add(ownerRow);

            middle.add(infoWrap, BorderLayout.CENTER);
            middle.add(housesPanel, BorderLayout.SOUTH);

            add(top, BorderLayout.NORTH);
            add(middle, BorderLayout.CENTER);
            add(tokens, BorderLayout.SOUTH);

            setBorder(normalBorder);
            setPreferredSize(new Dimension(isCorner ? 98 : 76, isCorner ? 98 : 76));
        }

        public void reset(int localId) {
            this.localPlayerId = localId;
            this.playersOnThis = new PlayerDTO[0];
            this.tile = null;
            stripe.setBackground(UIManager.getColor("Panel.background"));
            name.setText("");
            info.setText("");
            owner.setText("");
            mortgage.setText("");
            housesPanel.setCounts(0, 0);
            tokens.setIcon(null);
            tokens.setText("");
            typeIcon.setType("");
            setBackground(UIManager.getColor("Panel.background"));
            setToolTipText(null);
        }

        public void setTile(TileDTO t) {
            this.tile = t;
            refresh();
        }

        public void addPlayer(PlayerDTO p) {
            if (p == null) return;
            PlayerDTO[] arr = new PlayerDTO[playersOnThis.length + 1];
            for (int i = 0; i < playersOnThis.length; i++) arr[i] = playersOnThis[i];
            arr[playersOnThis.length] = p;
            playersOnThis = arr;
            refresh();
        }

        private void refresh() {
            if (tile == null) return;

            // Special backgrounds for corners + some types
            boolean isCorner = (index % 10 == 0);
            if (isCorner) {
                setBackground(new Color(0, 120, 215, 12));
            } else if ("CHANCE".equalsIgnoreCase(tile.type)) {
                setBackground(new Color(255, 200, 0, 12));
            } else if ("TAX".equalsIgnoreCase(tile.type)) {
                setBackground(new Color(220, 60, 60, 10));
            } else if ("JAIL".equalsIgnoreCase(tile.type)) {
                setBackground(new Color(120, 120, 120, 10));
            } else {
                setBackground(UIManager.getColor("Panel.background"));
            }

            stripe.setBackground(colorForGroup(tile.colorGroup, tile.type));

            name.setText(shorten(tile.name, isCorner ? 14 : 12));

            if ("PROPERTY".equalsIgnoreCase(tile.type)) {
                info.setText("$" + tile.price + " / R" + tile.rent);
            } else {
                info.setText(tile.type);
            }

            if ("PROPERTY".equalsIgnoreCase(tile.type) && tile.ownerId > 0) {
                owner.setText("Owner P" + tile.ownerId);
            } else {
                owner.setText("");
            }

            if ("PROPERTY".equalsIgnoreCase(tile.type) && tile.mortgaged) {
                mortgage.setText("M");
                mortgage.setForeground(new Color(160, 80, 0));
            } else {
                mortgage.setText("");
                mortgage.setForeground(UIManager.getColor("Label.foreground"));
            }

            // Houses / hotel markers (if your logic sets them later)
            if ("PROPERTY".equalsIgnoreCase(tile.type)) {
                housesPanel.setCounts(tile.houses, tile.hotels);
            } else {
                housesPanel.setCounts(0, 0);
            }

            typeIcon.setType(tile.type);

            tokens.setIcon(new TokensIcon(playersOnThis, localPlayerId));
            tokens.setText("");

            String tip = tile.name + " (#" + tile.index + ")";
            if ("PROPERTY".equalsIgnoreCase(tile.type)) {
                tip += " | Price $" + tile.price + " | Rent " + tile.rent;
                if (tile.ownerId > 0) tip += " | Owner P" + tile.ownerId;
                if (tile.mortgaged) tip += " | Mortgaged";
                if (tile.hotels > 0) tip += " | Hotel";
                if (tile.houses > 0) tip += " | Houses " + tile.houses;
            }
            setToolTipText(tip);
        }

        private String shorten(String s, int max) {
            if (s == null) return "";
            s = s.trim();
            if (s.length() <= max) return s;
            return s.substring(0, Math.max(0, max - 1)) + "…";
        }

        private Color colorForGroup(String group, String type) {
            if (group == null || !"PROPERTY".equalsIgnoreCase(type)) {
                return new Color(0, 0, 0, 15);
            }
            String g = group.toUpperCase();
            if (g.contains("BROWN")) return new Color(120, 72, 40);
            if (g.contains("LIGHT_BLUE") || g.contains("SKY")) return new Color(80, 160, 220);
            if (g.contains("PINK") || g.contains("MAGENTA")) return new Color(210, 80, 160);
            if (g.contains("ORANGE")) return new Color(230, 130, 40);
            if (g.contains("RED")) return new Color(200, 60, 60);
            if (g.contains("YELLOW")) return new Color(220, 200, 60);
            if (g.contains("GREEN")) return new Color(70, 160, 90);
            if (g.contains("BLUE") || g.contains("DARK_BLUE")) return new Color(60, 90, 200);
            return new Color(140, 140, 140);
        }
    }

    // Houses/Hotel markers (paint small rectangles)
    private static class HousesPanel extends JPanel {
        private int houses;
        private int hotels;

        public HousesPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(10, 12));
        }

        public void setCounts(int houses, int hotels) {
            this.houses = Math.max(0, houses);
            this.hotels = Math.max(0, hotels);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int x = 4;
                int y = 2;
                if (hotels > 0) {
                    g.setColor(new Color(200, 60, 60));
                    g.fillRoundRect(x, y, 16, 8, 4, 4);
                    g.setColor(new Color(0, 0, 0, 90));
                    g.drawRoundRect(x, y, 16, 8, 4, 4);
                } else {
                    g.setColor(new Color(70, 160, 90));
                    for (int i = 0; i < houses && i < 4; i++) {
                        g.fillRoundRect(x + i * 10, y, 8, 8, 3, 3);
                        g.setColor(new Color(0, 0, 0, 70));
                        g.drawRoundRect(x + i * 10, y, 8, 8, 3, 3);
                        g.setColor(new Color(70, 160, 90));
                    }
                }
            } finally {
                g.dispose();
            }
        }
    }

    // Type icon (simple vector)
    private static class TypeIcon implements Icon {
        private String type = "";

        public void setType(String type) {
            this.type = (type == null) ? "" : type.toUpperCase();
        }

        @Override
        public int getIconWidth() {
            return 14;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }

        @Override
        public void paintIcon(Component c, Graphics g0, int x, int y) {
            Graphics2D g = (Graphics2D) g0.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if ("GO".equals(type)) {
                    g.setColor(new Color(60, 130, 250));
                    g.fillOval(x + 2, y + 2, 10, 10);
                } else if ("JAIL".equals(type)) {
                    g.setColor(new Color(120, 120, 120));
                    g.fillRect(x + 3, y + 3, 8, 8);
                    g.setColor(new Color(255, 255, 255, 160));
                    g.drawLine(x + 5, y + 3, x + 5, y + 10);
                    g.drawLine(x + 7, y + 3, x + 7, y + 10);
                } else if ("CHANCE".equals(type)) {
                    g.setColor(new Color(230, 180, 60));
                    g.fillRoundRect(x + 2, y + 2, 10, 10, 4, 4);
                    g.setColor(new Color(255, 255, 255, 190));
                    g.setFont(g.getFont().deriveFont(Font.BOLD, 10f));
                    g.drawString("?", x + 5, y + 11);
                } else if ("TAX".equals(type)) {
                    g.setColor(new Color(240, 80, 80));
                    g.fillRoundRect(x + 2, y + 3, 10, 8, 4, 4);
                    g.setColor(new Color(255, 255, 255, 190));
                    g.setFont(g.getFont().deriveFont(Font.BOLD, 9f));
                    g.drawString("$", x + 5, y + 10);
                } else {
                    g.setColor(new Color(0, 0, 0, 35));
                    g.drawOval(x + 3, y + 3, 8, 8);
                }
            } finally {
                g.dispose();
            }
        }
    }

    // Player tokens icon (colored circles)
    private static class TokensIcon implements Icon {
        private final PlayerDTO[] players;
        private final int localPlayerId;

        public TokensIcon(PlayerDTO[] players, int localPlayerId) {
            this.players = (players == null) ? new PlayerDTO[0] : players;
            this.localPlayerId = localPlayerId;
        }

        @Override
        public int getIconWidth() {
            return 60;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }

        @Override
        public void paintIcon(Component c, Graphics g0, int x, int y) {
            Graphics2D g = (Graphics2D) g0.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int r = 5;
                int gap = 4;
                int startX = x + 2;
                int cy = y + 4;

                for (int i = 0; i < players.length && i < 4; i++) {
                    PlayerDTO p = players[i];
                    Color col = tokenColor(p.getId());
                    g.setColor(col);
                    int cx = startX + i * (2 * r + gap);
                    g.fillOval(cx, cy, 2 * r, 2 * r);

                    g.setColor(new Color(0, 0, 0, 90));
                    g.drawOval(cx, cy, 2 * r, 2 * r);

                    if (p.getId() == localPlayerId) {
                        g.setColor(Color.WHITE);
                        g.drawOval(cx + 1, cy + 1, 2 * r - 2, 2 * r - 2);
                    }
                }
            } finally {
                g.dispose();
            }
        }
    }

    public void initDefaultTokenColors() {
        tokenColors[1] = new Color(60, 130, 250);
        tokenColors[2] = new Color(240, 80, 80);
        tokenColors[3] = new Color(90, 180, 110);
        tokenColors[4] = new Color(230, 180, 60);
    }

    public void setTokenColor(int playerId, Color color) {
        if (playerId >= 1 && playerId <= 4 && color != null) {
            tokenColors[playerId] = color;
            repaint();
        }
    }

    private static Color tokenColor(int id) {
        if (id >= 1 && id <= 4 && tokenColors[id] != null) return tokenColors[id];
        return new Color(120, 120, 120);
    }

}
