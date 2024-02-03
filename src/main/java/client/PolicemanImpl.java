package client;

import javax.annotation.PostConstruct;

public class PolicemanImpl implements Policeman {


  @PostConstruct
  public void init() {}

  @Override
//  @Deprecated
  public void makePeopleLeaveRoom() {
    System.out.println("пиф паф, бах бах, кыш, кыш!");
  }
}
