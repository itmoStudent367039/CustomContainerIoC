package core;

import core.annotations.Lazy;
import core.configuration.JavaConfig;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Predicate;

public class Application {
  private static final String CONFIGURATORS_PATH = "core.configurators";

  public static ApplicationContext run(String packageToScan, Map<Class, Class> ifc2ImplClass) {
    JavaConfig config = new JavaConfig(packageToScan, ifc2ImplClass);
    ApplicationContext context = new ApplicationContext(config);
    ObjectFactory objectFactory = new ObjectFactory(context, CONFIGURATORS_PATH);
    // todo homework - init all singletons which are not lazy
    context.setFactory(objectFactory);
    initAllSingletons(context, config.getScanner());
    return context;
  }

  private static void initAllSingletons(ApplicationContext context, Reflections scanner) {
    scanner.getTypesAnnotatedWith(Lazy.class).stream()
        .filter(clazz -> isConcreteType(clazz) && !clazz.getAnnotation(Lazy.class).isLazy())
        .forEach(context::getObject);
  }

  private static boolean isConcreteType(Class<?> clazz) {
    return !(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()));
  }
}
