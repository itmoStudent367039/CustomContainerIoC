package core;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import core.configurators.ObjectConfigurator;
import core.configurators.ProxyConfigurator;
import lombok.SneakyThrows;
import org.reflections.Reflections;

public class ObjectFactory {
  private final ApplicationContext context;
  private final List<ObjectConfigurator> configurators = new ArrayList<>();
  private final List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

  @SneakyThrows
  ObjectFactory(ApplicationContext context, String pathToConfigurators) {
    this.context = context;
    initConfigurators(pathToConfigurators);
  }

  private void initConfigurators(String pathToConfigurators)
      throws InstantiationException,
          IllegalAccessException,
          InvocationTargetException,
          NoSuchMethodException {
    var scanner = new Reflections(pathToConfigurators);
    for (Class<? extends ObjectConfigurator> aClass :
        scanner.getSubTypesOf(ObjectConfigurator.class)) {
      configurators.add(aClass.getDeclaredConstructor().newInstance());
    }
    for (Class<? extends ProxyConfigurator> aClass :
        scanner.getSubTypesOf(ProxyConfigurator.class)) {
      proxyConfigurators.add(aClass.getDeclaredConstructor().newInstance());
    }
  }

  @SneakyThrows
  <T> T createObject(Class<T> implClass) {
    T t = create(implClass);
    configure(t);
    invokeInit(implClass, t);
    t = wrapWithProxyIfNeeded(implClass, t);
    return t;
  }

  private <T> T wrapWithProxyIfNeeded(Class<T> implClass, T t) {
    for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
      Object proxy = proxyConfigurator.replaceWithProxyIfNeeded(t, implClass);
      t = (T) proxy;
    }
    return t;
  }

  private <T> void invokeInit(Class<T> implClass, T t)
      throws IllegalAccessException, InvocationTargetException {
    for (Method method : implClass.getMethods()) {
      if (method.isAnnotationPresent(PostConstruct.class)) {
        method.invoke(t);
      }
    }
  }

  private <T> void configure(T t) {
    configurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
  }

  private <T> T create(Class<T> implClass)
      throws InstantiationException,
          IllegalAccessException,
          InvocationTargetException,
          NoSuchMethodException {
    return implClass.getDeclaredConstructor().newInstance();
  }
}
