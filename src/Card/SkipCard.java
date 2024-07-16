package Card;

import CardManager.CardManager;
import GameManager.GameManager;

public class SkipCard extends Card {
  public SkipCard(String _color) {
    super("Skip", _color);
  }

  @Override
  public void useAction(Card card) {
    CardManager cardManager = CardManager.getInstance();
    GameManager gameManager = GameManager.getInstance();
    gameManager.nextPlayer();
    cardManager.setTopCard(card);
  }

}