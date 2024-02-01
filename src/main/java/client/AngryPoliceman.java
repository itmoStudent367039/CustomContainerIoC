package client;

import core.annotations.Qualifier;

@Qualifier
public class AngryPoliceman implements Policeman {
  @Override
  public void makePeopleLeaveRoom() {
    System.out.println("Всех убью! Вон пошли");
  }
}
