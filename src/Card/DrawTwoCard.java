package Card;

import CardManager.CardManager;
import GameManager.GameManager;
import Player.Player;

public class DrawTwoCard extends Card {
  public DrawTwoCard(String _color) {
    super("DrawTwo", _color);
  }

  @Override
  public void useAction(Card card) {
    CardManager cardManager = CardManager.getInstance();
    GameManager gameManager = GameManager.getInstance();
    Player player = gameManager.getNextPlayer();
    cardManager.drawCards(player, 2);
    cardManager.setTopCard(card);
  }
}