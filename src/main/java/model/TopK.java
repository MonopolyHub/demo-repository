package model;

import dataStructures.heap.DataAsset;
import dataStructures.heap.MaxHeap;

public class TopK {
    Player[] players;

    public TopK(Player[] players) {
        this.players = players;
    }

    public void richestPlayersList() {
        MaxHeap maxHeap = new MaxHeap(players.length);
        for (Player player : players) {
            if (player.getStatus() != Player.Status.BANKRUPT) {
                DataAsset dataAsset = new DataAsset(player, player.getBalance());
                maxHeap.insert(dataAsset);
            }
        }
        for (int i = 0; i <= 2; i++) {
            maxHeap.extractMax();
            //log
        }
    }

    public void highestRentalIncome() {
        MaxHeap maxHeap = new MaxHeap(players.length);
        for (Player player : players) {
            if (player.getStatus() != Player.Status.BANKRUPT) {
                DataAsset dataAsset = new DataAsset(player, player.getProfitRent());
                maxHeap.insert(dataAsset);
            }
        }
        for (int i = 0; i <= 2; i++) {
            maxHeap.extractMax();
            //log
        }
    }

    public void update() {
        richestPlayersList();
        highestRentalIncome();
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

}
