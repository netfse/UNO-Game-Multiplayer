package Card;

import CardManager.CardManager;
import GameManager.GameManager;
import Player.Player;

public class WildDrawFourCard extends Card {
  public WildDrawFourCard() {
    super("WildDrawFour", "Wild");
  }

  @Override
  public void useAction(Card card) {
    CardManager cardManager = CardManager.getInstance();
    GameManager gameManager = GameManager.getInstance();
    Player player = gameManager.getNextPlayer();
    cardManager.drawCards(player, 4);
    cardManager.setTopCard(card);
  }
}