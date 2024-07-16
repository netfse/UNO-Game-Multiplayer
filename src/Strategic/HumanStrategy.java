package Strategic;

import Card.Card;
import Player.*;
import Server.UNOServer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class HumanStrategy implements Strategic {

  @Override
  public Card chooseCards(Player player, ArrayList<Card> cards, Card cardOnTop) {
    String message = "Your deck: ";
    for (int i = 0; i < cards.size(); i++) {
      message += cards.get(i);
      if (i < cards.size() - 1)
        message += ", ";
    }
    message += "\n";

    UNOServer.printMessageToOne(player.getSocket(), message);
    UNOServer.printMessageToOne(player.getSocket(), "Your move (input draw, to draw a card): ");

    String playerMove = UNOServer.recieveMessageFromOne(player.getSocket());

    while (!playerMove.equals("draw") && playerMove.split(" ").length < 2) {
      UNOServer.printMessageToOne(player.getSocket(), "Please enter a valid value (Input format: Number Color): ");
      playerMove = UNOServer.recieveMessageFromOne(player.getSocket());
    }

    // return input
    if (playerMove.equals("draw")) {
      return null;
    } else {
      String[] input = playerMove.split(" ");
      return new Card(input[0], input[1]);
    }
  }

  @Override
  public Boolean callUNO(Player player, int timeLimit) {
    AtomicBoolean inputFailed = new AtomicBoolean(false);

    Thread timerThread = new Thread(() -> {
      try {
        Thread.sleep(timeLimit);
        UNOServer.printMessageToOne(player.getSocket(), "Call UNO Failed. Press Enter key to continue...");
        inputFailed.set(true);
      } catch (InterruptedException e) {
        UNOServer.printMessageToOne(player.getSocket(), "Call UNO Successfully.");
      }
    });

    String userInput = "";
    timerThread.start();

    while (!inputFailed.get() && !userInput.equals("UNO")) {
      UNOServer.printMessageToOne(player.getSocket(), "Type 'UNO': ");
      userInput = UNOServer.recieveMessageFromOne(player.getSocket());
    }

    timerThread.interrupt();
    return userInput.equals("UNO") && !inputFailed.get();
  }
}
