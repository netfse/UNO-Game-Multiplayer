package Strategic;

import Player.*;
import Card.Card;
import java.util.ArrayList;
import java.util.Random;

public class AIStrategy implements Strategic {
  @Override
  public Card chooseCards(Player player, ArrayList<Card> cards, Card cardOnTop) {
    Card selectedCard = null;
    for (Card card : cards) {
      if (card.getName().equals("Wild")) {
        String[] colors = { "Red", "Green", "Blue", "Yellow" };
        Random random = new Random();
        String randomColor = colors[random.nextInt(colors.length)];

        selectedCard = new Card("Wild", randomColor);
        return selectedCard;
      }
    }

    ArrayList<Card> matchingActionCards = new ArrayList<>();
    for (Card card : cards) {
      if (card.getName().equals("DrawTwo") ||
          card.getName().equals("Reverse") ||
          card.getName().equals("Skip")) {
        if (card.getColor().equals(cardOnTop.getColor())) {
          matchingActionCards.add(card);
        }
      }
    }

    if (!matchingActionCards.isEmpty()) {
      Random random = new Random();
      int randomIndex = random.nextInt(matchingActionCards.size());
      selectedCard = matchingActionCards.get(randomIndex);
      return selectedCard;
    }

    for (Card card : cards) {
      if (card.getName().equals("WildDrawFour")) {
        String[] colors = { "Red", "Green", "Blue", "Yellow" };
        Random random = new Random();
        String randomColor = colors[random.nextInt(colors.length)];

        selectedCard = new Card("WildDrawFour", randomColor);
        return selectedCard;
      }
    }

    ArrayList<Card> matchingCards = new ArrayList<>();
    for (Card card : cards) {
      if (card.getColor().equals(cardOnTop.getColor()) ||
          card.getName() == cardOnTop.getName()) {
        matchingCards.add(card);
      }
    }

    if (!matchingCards.isEmpty()) {
      Random random = new Random();
      int randomIndex = random.nextInt(matchingCards.size());
      selectedCard = matchingCards.get(randomIndex);
      return selectedCard;
    }

    return selectedCard;
  }

  @Override
  public Boolean callUNO(Player player, int timeLimit) {
    return true;
  }
}
