package Card;

import CardManager.CardManager;
import GameManager.GameManager;

public class ReverseCard extends Card {

  public ReverseCard(String _color) {
    super("Reverse", _color);
  }

  @Override
  public void useAction(Card card) {
    CardManager cardManager = CardManager.getInstance();
    GameManager gameManager = GameManager.getInstance();
    gameManager.changeDirection();
    cardManager.setTopCard(card);
  }

}