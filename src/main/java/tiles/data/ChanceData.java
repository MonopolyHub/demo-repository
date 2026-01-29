package tiles.data;

import dataStructures.queue.Queue;
import model.CardType;
import model.Player;

import java.util.Random;


public class ChanceData extends TileData {
    Queue<CardType> cards;

    public ChanceData() {
        cards = new Queue<>();
        intializeCards();
    }

    public CardType runCard(Player player) {
        // FIFO: take from front and put it back to end (as required by PDF)
        CardType card = cards.dequeue();
        cards.enqueue(card);
        return card;
    }

    public void intializeCards() {
        Random rand = new Random();
        CardType[] values = CardType.values();
        // fixed number of cards to keep it simple
        for (int i = 0; i < 16; i++) {
            CardType card = values[rand.nextInt(values.length)];
            cards.enqueue(card);
        }
    }
}