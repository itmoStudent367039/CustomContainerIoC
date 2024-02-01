package client;

import core.annotations.InjectByType;

//@Deprecated
public class CoronaDesinfector {

  @InjectByType private Announcer announcer;
  @InjectByType private Policeman policeman;

//  @Deprecated
  public void start(Room room) {
    announcer.announce("Начинаем дезинфекцию, всё вон!");
    policeman.makePeopleLeaveRoom();
    desinfect(room);
    announcer.announce("Рискните зайти обратно");
  }
  private void desinfect(Room room) {
    System.out.println(
        "зачитывается молитва: 'корона изыди!' - молитва прочитана, вирус низвергнут в ад");
  }
}
