package GameManager;

import Server.UNOServer;
import CardManager.CardManager;
import Player.Player;
import java.util.ArrayList;
import java.util.Random;

public class GameManager {

  private static GameManager gameManager;
  private ArrayList<Player> playerList;
  private int direction;
  private int index;
  private boolean gameOver;

  protected GameManager() {
    playerList = new ArrayList<>();
    direction = 1;
    index = 0;
    gameOver = false;
  }

  public static GameManager getInstance() {
    if (gameManager == null)
      gameManager = new GameManager();
    return gameManager;
  }

  public void setPlayerList(ArrayList<Player> playerList) {
    this.playerList = playerList;
  }

  public void clearPlayerList() {
    this.playerList.clear();
  }

  public Player getCurrentPlayer() {
    return playerList.get(index);
  }

  public void setGameOver() {
    this.gameOver = true;
  }

  public boolean isGameOver() {
    return this.gameOver;
  }

  public Player getNextPlayer() {
    int nextIndex;

    if (playerList.size() <= 0) {
      return null;
    }

    // Clockwise player list
    if (this.direction == 1) {
      if (this.index + 1 >= playerList.size()) {
        nextIndex = 0;
      } else {
        nextIndex = this.index + 1;
      }
    }
    // Anti-clockwise player list
    else {
      if (this.index - 1 < 0) {
        nextIndex = playerList.size() - 1;
      } else {
        nextIndex = this.index - 1;
      }
    }

    Player nextPlayer = playerList.get(nextIndex);
    return nextPlayer;
  }

  public Player nextPlayer() {
    Player nextPlayer = getNextPlayer();
    this.index = playerList.indexOf(nextPlayer);
    return nextPlayer;
  }

  public void changeDirection() {
    if (this.direction == -1)
      this.direction = 1;
    else
      this.direction = -1;

  }

  public void startTurn(CardManager cardManager) {
    Player currentPlayer = getCurrentPlayer();

    UNOServer.printMessageToAll("\n" + currentPlayer.getName() + "'s turn.");
    UNOServer.printMessageToAll("The current card is: " + cardManager.getCardOnTop());

    currentPlayer.executeStrategy();

    // check if the player remains one card
    if (currentPlayer.remainsOneCard()) {
      UNOServer.printMessageToAll("\n" + currentPlayer.getName() + " remains one card!");
      callUNO(cardManager, currentPlayer);
    }
  }

  public void announceWinner(CardManager cardManager, Player player) {
    UNOServer.printMessageToAll(
        "\n==============================================\n" +
            "[Winner Announcement]\nCongratulations, " + player.getName() + " is the game-winner!" +
            "\n==============================================\n");
    UNOServer.printMessageToAll("[ScoreBoard]");
    int index = 1;
    printScoreBoard(cardManager, index);
  }

  private void callUNO(CardManager cardManager, Player player) {
    Random random = new Random();
    int timeLimit = random.nextInt(12001) + 6000;

    boolean current_player_callUNO = player.executeCallUNO(timeLimit);

    if (!current_player_callUNO) {
      UNOServer.printMessageToOne(player.getSocket(), "Penalty: ");
      cardManager.drawCards(player, 2);
    }
  }

  private void printScoreBoard(CardManager cardManager, int index) {
    if (playerList.size() != 0) {
      Player target = playerList.get(0);
      for (Player player : playerList) {
        if (cardManager.getTotalScore(player) <= cardManager.getTotalScore(target)) {
          target = player;
        }
      }
      UNOServer.printMessageToAll(
          index + ": " + target.getName() + " has " + -1 * cardManager.getTotalScore(target) + " scores");
      playerList.remove(target);
      printScoreBoard(cardManager, ++index);
    }
  }
}
