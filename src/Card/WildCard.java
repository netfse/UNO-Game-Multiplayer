package Card;

import CardManager.CardManager;

public class WildCard extends Card {
  public WildCard() {
    super("Wild", "Wild");
  }

  @Override
  public void useAction(Card card) {
    CardManager cardManager = CardManager.getInstance();
    cardManager.setTopCard(card);
  }

}