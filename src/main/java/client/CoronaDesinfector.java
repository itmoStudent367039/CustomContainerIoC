package client;

import core.annotations.InjectByType;
import core.annotations.Lazy;
import core.annotations.Singleton;

// @Deprecated
@Singleton
@Lazy(isLazy = false)
public class CoronaDesinfector {

  @InjectByType private Announcer announcer;
  @InjectByType private Policeman policeman;
  @InjectByType private Recommendator recommendator;

  public CoronaDesinfector() {
    System.out.println("NotLasy!");
  }

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
