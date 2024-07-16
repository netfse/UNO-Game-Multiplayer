package Server;

import Card.Card;
import CardManager.CardManager;
import GameManager.GameManager;
import Player.HumanPlayer;
import Player.Player;
import Strategic.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class UNOServer {
  private static List<Socket> clients = new ArrayList<>();
  private static Map<Socket, String> clientNames = new HashMap<>();
  private static int humans = 1;
  private static int ais = 1;

  public static void main(String[] args) throws IOException {
    try (ServerSocket ServerSocket = new ServerSocket(8080)) {
      System.out.println("UNO server started on port 8080");
      Scanner scanner = new Scanner(System.in);
      System.out.print("Please input how many humans: ");
      while (!scanner.hasNextInt()) {
        System.out.print("Please input how many humans: ");
        scanner.next();
      }
      humans = scanner.nextInt();

      System.out.print("Please input how many AI: ");
      while (!scanner.hasNextInt()) {
        System.out.print("Please input how many AI: ");
        scanner.next();
      }
      ais = scanner.nextInt();

      if (humans > 1)
        System.out.printf("Waiting for %d players joinning...\n", humans);
      else
        System.out.printf("Waiting for %d player joinning...\n", humans);

      while (clients.size() < humans) {
        Socket clientSocket = ServerSocket.accept();
        System.out.println("New client connected: " + clientSocket);

        clients.add(clientSocket);

        ServerThread serverThread = new ServerThread(clientSocket);

        serverThread.start();
      }

      for (Socket client : clients) {
        String name = clientNames.get(client);
        while (name == null) {
          name = clientNames.get(client);
        }
      }

      printMessageToAll("Let's get started!");

      // intialize players
      ArrayList<Player> playerList = new ArrayList<>();

      for (Socket client : clients)
        playerList.add(new HumanPlayer(client, new ArrayList<Card>(), new HumanStrategy(), clientNames.get(client)));

      for (int i = 0; i < ais; i++)
        playerList.add(new Player(new ArrayList<Card>(), new AIStrategy(), "Ai_" + i));

      printMessageToAll("\nShuffling cards...");
      printMessageToAll("Dealing cards...\n");

      Collections.shuffle(playerList);
      CardManager cardManager = CardManager.getInstance();

      for (Player player : playerList)
        cardManager.drawCards(player, 7);

      GameManager game = GameManager.getInstance();
      game.setPlayerList(playerList);

      printMessageToAll("\nThe cards have been distributed. Let's Begin!");

      while (!game.isGameOver()) {
        game.startTurn(cardManager);

        for (Player player : playerList) {
          if (player.hasWon()) {
            printMessageToAll("\n" + player.getName() + " has won the game!");
            game.announceWinner(cardManager, player);
            game.setGameOver();
            break;
          }
        }
        game.nextPlayer();
      }
      
      System.out.println("The game is over!");
      System.out.println("Type Exit to close the server!");
      while (scanner.hasNext()) {
        if (scanner.next().equals("Exit")) {
          break;
        }
      }
      ServerSocket.close();
      scanner.close();
      System.exit(0);
    }
  }

  public static void printMessageToOne(Socket socket, String message) {
    try {
      PrintWriter printMessageToOne = new PrintWriter(
          socket.getOutputStream(),
          true);
      printMessageToOne.println(message);
      System.out.println("[printMessageToOne] " + message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void printMessageToAll(String message) {
    try {
      for (Socket socket : clients) {
        PrintWriter out = new PrintWriter(
            socket.getOutputStream(),
            true);
        out.println(message);
        System.out.println("[printMessageToAll] " + message);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String recieveMessageFromOne(Socket clientSocket) {
    String clientMessage = "";
    try {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
      clientMessage = in.readLine();
      System.out.println("[clientMessage] " + clientMessage);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return clientMessage;
  }

  private static class ServerThread extends Thread {

    private Socket clientSocket;

    public ServerThread(Socket clientSocket) {
      this.clientSocket = clientSocket;
    }

    public void run() {
      try {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        out.println("Welcome to the UNO server!");
        out.println("Please enter your name:");
        String clientName = in.readLine();
        clientNames.put(clientSocket, clientName);

        for (Socket socket : clients) {
          if (socket != clientSocket) {
            PrintWriter out2 = new PrintWriter(socket.getOutputStream(), true);
            out2.println("Player " + clientName + " has joined the game!");
          }
        }

        GameManager game = GameManager.getInstance();
        while (!game.isGameOver())
          ;
        in.close();
        out.close();
        clients.remove(clientSocket);
        clientSocket.close();
        System.out.println("Client disconnected: " + clientSocket);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
