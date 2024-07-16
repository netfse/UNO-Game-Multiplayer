package CardManager;

import Card.*;
import Server.UNOServer;
import Player.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class CardManager {

  private Card topCard;
  private String[] colors = { "Red", "Green", "Yellow", "Blue" };
  private Stack<Card> cardStack = new Stack<>();
  private Stack<Card> cardPlayed = new Stack<>();
  private static CardManager cardManager;
  private long seed;

  private CardManager() {
    initializeNormalCards();
    initializeActionCards();
    seed = getRandomSeed();
    shuffleCard();
    setFirstTopCard();
  }

  public static CardManager getInstance() {
    if (cardManager == null)
      cardManager = new CardManager();
    return cardManager;
  }

  private void initializeNormalCards() {
    for (int i = 0; i <= 9; i++) {
      int count = 2;
      if (i == 0) {
        count = 1;
      }
      for (String color : colors) {
        for (int j = 0; j < count; j++) {
          cardStack.add(new Card("" + i, color));
        }
      }
    }
  }

  private void initializeActionCards() {
    int drawTwoNum = 2;
    int skipNum = 2;
    int reverseNum = 2;
    int wildNum = 4;
    int wildDrawFourNum = 4;

    for (String color : colors) {
      for (int i = 0; i < drawTwoNum; i++) {
        cardStack.add(new DrawTwoCard(color));
      }
    }
    for (String color : colors) {
      for (int i = 0; i < skipNum; i++) {
        cardStack.add(new SkipCard(color));
      }
    }
    for (String color : colors) {
      for (int i = 0; i < reverseNum; i++) {
        cardStack.add(new ReverseCard(color));
      }
    }
    for (int i = 0; i < wildNum; i++) {
      cardStack.add(new WildCard());
    }
    for (int i = 0; i < wildDrawFourNum; i++) {
      cardStack.add(new WildDrawFourCard());
    }
  }

  public void drawCards(Player player, int number) {
    String toPrint = player.getName() + " draws " + number;
    toPrint += number == 1 ? " card" : " cards";
    UNOServer.printMessageToAll(toPrint);
    for (int i = 0; i < number; i++) {
      player.addCard(cardStack.get(cardStack.size() - 1));
      cardStack.remove(cardStack.size() - 1);
      if (cardStack.size() <= 0) {
        putCardBack();
      }
    }
  }

  public void putCardBack() {
    while (!cardPlayed.isEmpty()) {
      Card toMove = cardPlayed.get(cardPlayed.size() - 1);
      if (toMove.getName().equals("WildDrawFour") ||
          toMove.getName().equals("Wild")) {
        toMove.setColor("Wild");
      }
      cardPlayed.remove(cardPlayed.size() - 1);
      cardStack.add(toMove);
    }
    shuffleCard();
  }

  public void shuffleCard() {
    int stackSize = cardStack.size();
    Random randomGenerator = new Random(seed);
    for (int i = 0; i < stackSize; i++) {
      int randomNum = randomGenerator.nextInt(stackSize);
      Collections.swap(cardStack, i, randomNum);
    }
  }

  public Card getCardOnTop() {
    return topCard;
  }

  public void recordCardPlayed(Card card) {
    cardPlayed.add(card);
    // setTopCard(card);
  }

  public void setTopCard(Card card) {
    topCard = card;
  }

  private void setFirstTopCard() {
    Card randomCard;
    do {
      randomCard = cardStack.pop();
      cardStack.push(randomCard);
    } while (randomCard.getColor().equals("Wild"));
    cardPlayed.push(randomCard);
    topCard = randomCard;
  }

  private long getRandomSeed() {
    Random randomGenerator = new Random();
    return randomGenerator.nextLong();
  }

  public int getTotalScore(Player player) {
    ArrayList<Card> cards = player.getCards();
    int totalScore = 0;
    for (Card card : cards) {
      switch (card.getName()) {
        case "0":
        case "1":
        case "2":
        case "3":
        case "4":
        case "5":
        case "6":
        case "7":
        case "8":
        case "9":
          totalScore += Integer.parseInt(card.getName());
          break;
        case "Skip":
        case "Reverse":
        case "DrawTwo":
          totalScore += 20;
          break;
        case "Wild":
        case "WildDrawFour":
          totalScore += 50;
          break;
      }
    }
    return totalScore;
  }
}
