import Server.UNOServer;
import java.io.*;

public class Main {

  public static void main(String args[]) throws java.io.IOException {
    try {
      UNOServer.main(args);
    } catch (IOException e) {
      System.out.println("The port is already used.");
      // e.printStackTrace();
    }
  }
}
