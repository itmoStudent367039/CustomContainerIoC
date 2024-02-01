package client;

import core.annotations.InjectByType;

public class ConsoleAnnouncer implements Announcer {
  @InjectByType
  private Recommendator recommendator;

  @Override
  public void announce(String message) {
    System.out.println(message);
    recommendator.recommend();
  }
}