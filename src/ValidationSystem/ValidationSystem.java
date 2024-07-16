package ValidationSystem;

import Card.Card;
import CardManager.CardManager;
import Player.Player;

public class ValidationSystem {
  private static ValidationSystem validationSystem;

  protected ValidationSystem() {
  }

  static public ValidationSystem getInstance() {
    if (validationSystem == null)
      validationSystem = new ValidationSystem();
    return validationSystem;
  }

  public boolean checkCanPlay(String[] input, CardManager cardManager) {
    Card cardOnTop = cardManager.getCardOnTop();

    // check is same number or action card
    if (cardOnTop.getName().equals(input[0])) {
      return true;
    }
    // check is same color card
    else if (cardOnTop.getColor().equals(input[1])) {
      return true;
    }
    // check is a Wild card
    else if ("Wild".equals(input[0])) {
      switch (input[1]) {
        case "Red":
        case "Blue":
        case "Green":
        case "Yellow":
          return true;
      }
    }
    // check is a WildDrawFour card
    else if ("WildDrawFour".equals(input[0])) {
      switch (input[1]) {
        case "Red":
        case "Blue":
        case "Green":
        case "Yellow":
          return true;
      }
    }
    return false;
  }

  public boolean checkIfCardExists(String[] input, Player player) {
    if (player.findCard(input[0], input[1]) != null) {
      return true;
    }
    return false;
  }

  public boolean checkCanPlayDrawFour(Player player, Card cardOnTop) {
    String cardOnTopColor = cardOnTop.getColor();
    String cardOnTopName = cardOnTop.getName();

    if (player.findCardsByColor(cardOnTopColor).size() > 0) {
      return false;
    } else if (player.findCardsByName(cardOnTopName).size() > 0) {
      return false;
    } else if (player.findCard("WildDrawFour", "Wild") == null) {
      return false;
    }
    return true;
  }
}