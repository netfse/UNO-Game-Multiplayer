package Server;

import java.io.*;
import java.net.*;

public class UNOClient {
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.out.println("Usage: java UNOClient <host> <port>");
      System.exit(1);
    }

    String host = args[0];
    int port = Integer.parseInt(args[1]);

    Socket socket = new Socket(host, port);
    System.out.println("Connected to the chat server: " + socket);

    BufferedReader in = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    Thread receiver = new Thread(
        new Runnable() {
          public void run() {
            try {
              while (true) {
                String message = in.readLine();

                if (message == null) {
                  break;
                }

                System.out.println(message);
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });

    receiver.start();

    while (true) {
      String message = stdIn.readLine();
      if (message.equals("quit")) {
        System.out.println("Disconnected from the chat server: " + socket);
        break;
      }
      out.println(message);
    }
    in.close();
    out.close();
    stdIn.close();
    socket.close();
  }
}
