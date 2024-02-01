package core;

import core.configuration.JavaConfig;

import java.util.Map;

public class Application {
  private static final String CONFIGURATORS_PATH = "core.configurators";

  public static ApplicationContext run(String packageToScan, Map<Class, Class> ifc2ImplClass) {
    JavaConfig config = new JavaConfig(packageToScan, ifc2ImplClass);
    ApplicationContext context = new ApplicationContext(config);
    ObjectFactory objectFactory = new ObjectFactory(context, CONFIGURATORS_PATH);
    // todo homework - init all singletons which are not lazy
    context.setFactory(objectFactory);
    return context;
  }
}
