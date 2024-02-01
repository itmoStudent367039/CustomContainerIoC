import client.CoronaDesinfector;
import client.Room;
import core.Application;
import core.ApplicationContext;

import java.util.Collections;
import java.util.HashMap;

public class Main {
  public static void main(String[] args) {
    ApplicationContext context = Application.run("client", new HashMap<>(Collections.emptyMap()));
    CoronaDesinfector desinfector = context.getObject(CoronaDesinfector.class);
    desinfector.start(new Room());
  }
}
