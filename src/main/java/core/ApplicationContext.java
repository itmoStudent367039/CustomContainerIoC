package core;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import core.annotations.Singleton;
import core.configuration.Config;
import lombok.Setter;

public class ApplicationContext {
  @Setter private ObjectFactory factory;
  private final Map<Class, Object> cache = new ConcurrentHashMap<>();
  private final Config config;

  public ApplicationContext(Config config) {
    this.config = config;
  }

  public <T> T getObject(Class<T> type) {
    if (cache.containsKey(type)) {
      return (T) cache.get(type);
    }

    Class<? extends T> implClass = getImplClass(type);

    T t = factory.createObject(implClass);

    if (implClass.isAnnotationPresent(Singleton.class)) {
      cache.put(type, t);
    }

    return t;
  }

  private <T> Class<? extends T> getImplClass(Class<T> type) {
    Class<? extends T> implClass = type;
    if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
      implClass = config.getImplClass(type);
    }
    return implClass;
  }
}
