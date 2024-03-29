package client;

import core.annotations.InjectProperty;
import core.annotations.Singleton;

@Singleton
public class RecommendatorImpl implements Recommendator {
  @InjectProperty("wisky")
  private String alcohol;

  public RecommendatorImpl() {
    System.out.println("recommendator was created");
  }

  @Override
//  @Deprecated
  public void recommend() {
    System.out.println("to protect from covid-2019, drink " + alcohol);
  }
}
