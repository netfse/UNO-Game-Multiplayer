package Player;

import java.net.*;

import Card.Card;
import Strategic.Strategic;
import java.util.ArrayList;

public class HumanPlayer extends Player {

  private Socket client;

  public HumanPlayer(Socket client, ArrayList<Card> cards, Strategic strategy, String name) {
    super(cards, strategy, name);
    this.client = client;
  }

  public Socket getSocket() {
    return client;
  }
}
