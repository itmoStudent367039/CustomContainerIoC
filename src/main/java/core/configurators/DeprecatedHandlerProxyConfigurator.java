package core.configurators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.sf.cglib.proxy.Enhancer;

public class DeprecatedHandlerProxyConfigurator implements ProxyConfigurator {
  @Override
  public Object replaceWithProxyIfNeeded(Object t, Class<?> implClass) {
    if (implClass.isAnnotationPresent(Deprecated.class)) {
      return getProxy(t, implClass, (method) -> true);
    }

    Set<Method> deprecatedMethods = getDeprecatedMethods(implClass);
    if (deprecatedMethods.isEmpty()) return t;
    return getProxy(t, implClass, (deprecatedMethods::contains));
  }

  private Object getProxy(Object t, Class<?> implClass, Predicate<Method> changeLogic) {
    //    if (implClass.getInterfaces().length == 0) {
    return Enhancer.create(
        implClass,
        (net.sf.cglib.proxy.InvocationHandler)
            (proxy, method, args) -> proxyInvocationLogic(method, args, changeLogic, t));
    //    }

    //    return Proxy.newProxyInstance(
    //            implClass.getClassLoader(),
    //            implClass.getInterfaces(),
    //            (proxy, method, args) -> proxyInvocationLogic(method, args, changeLogic, t));
  }

  private Object proxyInvocationLogic(
      Method method, Object[] args, Predicate<Method> changeLogic, Object t)
      throws IllegalAccessException, InvocationTargetException {
    return changeLogic.test(method)
        ? getInvocationHandlerLogic(method, args, t)
        : method.invoke(t, args);
  }

  private Object getInvocationHandlerLogic(Method method, Object[] args, Object t)
      throws IllegalAccessException, InvocationTargetException {
    System.out.printf(
        "Using deprecated method - %s(), class - %s%n",
        method.getName(), method.getDeclaringClass().getName());
    return method.invoke(t, args);
  }

  private Set<Method> getDeprecatedMethods(Class<?> implClass) {
    return Arrays.stream(implClass.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(Deprecated.class))
        .collect(Collectors.toSet());
  }
}
