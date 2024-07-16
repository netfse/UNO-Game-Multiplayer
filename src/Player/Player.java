package Player;

import Server.UNOServer;

import Card.Card;
import CardManager.CardManager;
import Strategic.Strategic;
import ValidationSystem.ValidationSystem;

import java.net.Socket;
import java.util.ArrayList;

public class Player {

  private ArrayList<Card> cards;
  private Strategic strategy;
  private String name;

  public Player(ArrayList<Card> cards, Strategic strategy, String name) {
    this.cards = cards;
    this.strategy = strategy;
    this.name = name;
  }

  public boolean hasWon() {
    return cards.isEmpty();
  }

  public boolean remainsOneCard() {
    return cards.size() == 1;
  }

  public ArrayList<Card> getCards() {
    return this.cards;
  }

  public String getName() {
    return this.name;
  }

  public int getNumberOfCards() {
    return cards.size();
  }

  public Card findCard(String cardName, String cardColor) {
    for (Card c : cards) {
      // find a same card
      if (c.getColor().equals(cardColor) && c.getName().equals(cardName)) {
        return c;
      }
      // find a WildDrawFour card
      else if (c.getName().equals(cardName) && c.getName().equals("WildDrawFour")) {
        switch (cardColor) {
          case "Red":
          case "Blue":
          case "Green":
          case "Yellow":
            c.setColor(cardColor);
            return c;
          default:
            continue;
        }
      }
      // find a Wild card
      else if (c.getName().equals(cardName) && c.getName().equals("Wild")) {
        switch (cardColor) {
          case "Red":
          case "Blue":
          case "Green":
          case "Yellow":
            c.setColor(cardColor);
            return c;
          default:
            continue;
        }
      }
    }
    return null;
  }

  public ArrayList<Card> findCardsByName(String cardName) {
    ArrayList<Card> result = new ArrayList<>();
    for (Card c : cards) {
      if (c.getName().equals(cardName)) {
        result.add(c);
      }
    }
    return result;
  }

  public ArrayList<Card> findCardsByColor(String cardColor) {
    ArrayList<Card> result = new ArrayList<>();
    for (Card c : cards) {
      if (c.getColor().equals(cardColor)) {
        result.add(c);
      }
    }
    return result;
  }

  private void playCard(Card card, CardManager cardSystem) {
    removeCard(card);
    cardSystem.recordCardPlayed(card);

    String message = String.format("%s plays %s and remains %s card", name, card, getNumberOfCards());

    if (getNumberOfCards() > 1)
      message += "s.";
    else
      message += ".";

    UNOServer.printMessageToAll(message);

    card.useAction(card);
  }

  public void removeCard(Card card) {
    cards.remove(card);
  }

  public void addCard(Card card) {
    cards.add(card);
  }

  public void executeStrategy() {
    boolean validMove = false;

    while (validMove == false) {
      Card selectedCard = strategy.chooseCards(
          this,
          cards,
          CardManager.getInstance().getCardOnTop());
      if (selectedCard == null) {
        CardManager.getInstance().drawCards(this, 1);
        validMove = true;
      } else {
        ValidationSystem validateSys = ValidationSystem.getInstance();
        String input[] = { selectedCard.getName(), selectedCard.getColor() };

        if (!validateSys.checkIfCardExists(input, this)) {
          UNOServer.printMessageToOne(getSocket(), "Invalid card. Try again.\n");
          continue;
        } else if (!validateSys.checkCanPlay(input, CardManager.getInstance())) {
          UNOServer.printMessageToOne(getSocket(), "Invalid move. Try again.\n");
          continue;
        } else {
          playCard(
              findCard(selectedCard.getName(), selectedCard.getColor()),
              CardManager.getInstance());
          validMove = true;
        }
      }
    }
  }

  public boolean executeCallUNO(int timeLimit) {
    Boolean UNOIsCalled = strategy.callUNO(this, timeLimit);
    if (UNOIsCalled) {
      UNOServer.printMessageToAll(
          "\n==============================================\n" +
              "[UNO Announcement]\n" + getName() + " calls UNO!" +
              "\n==============================================\n");
      return true;
    }
    return false;
  }

  public Socket getSocket() {
    return null;
  }
}
