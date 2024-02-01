package core.configurators;

import core.ApplicationContext;

public interface ObjectConfigurator {
  void configure(Object t, ApplicationContext context);
}
