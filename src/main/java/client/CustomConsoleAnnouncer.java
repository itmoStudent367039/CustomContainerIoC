package client;

public class CustomConsoleAnnouncer implements ConsoleAnnouncer {
  @Override
  public void announce(String message) {
    System.out.print(message);
    System.out.println(" Hello from custom");
  }
}
