package core.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.annotations.Qualifier;
import org.reflections.Reflections;

public class JavaConfig implements Config {

  private final Reflections scanner;
  private final Map<Class, Class> ifc2ImplClass;

  public JavaConfig(String packageToScan, Map<Class, Class> ifc2ImplClass) {
    this.ifc2ImplClass = ifc2ImplClass;
    this.scanner = new Reflections(packageToScan);
  }

  @Override
  public <T> Class<? extends T> getImplClass(Class<T> ifc) throws RuntimeException {
    return ifc2ImplClass.computeIfAbsent(ifc, this::getImplementation);
  }

  private <T> Class<? extends T> getImplementation(Class<T> ifc) throws RuntimeException {
    Collection<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);

    if (classes.isEmpty()) throw new RuntimeException("Ambiguity - " + ifc + " has 0 implements");

    if (classes.size() == 1) {
      return classes.iterator().next();
    }

    return getQualifierImplementation(ifc, classes);
  }

  private <T> Class<? extends T> getQualifierImplementation(
      Class<T> ifc, Collection<Class<? extends T>> classes) {
    List<Class<? extends T>> list = getQualifierClasses(classes);
    throwsIfAmbiguity(ifc, classes, list);

    return list.iterator().next();
  }

  private <T> void throwsIfAmbiguity(
      Class<T> ifc, Collection<Class<? extends T>> classes, List<Class<? extends T>> list)
      throws RuntimeException {
    String message = null;
    if (list.isEmpty()) {
      message =
          String.format(
              "Ambiguity in implementation - %s has %s implementations. Use `Qualifier` annotation or update config!",
              ifc.getName(), classes.size());
    } else if (list.size() > 1) {
      message =
          String.format(
              "Ambiguity in implementation - %s has %s `Qualifier` annotated implementations!",
              ifc.getName(), list.size());
    }
    if (message != null) throw new RuntimeException(message);
  }

  private <T> List<Class<? extends T>> getQualifierClasses(Collection<Class<? extends T>> classes) {
    return classes.stream()
        .filter(clazz -> clazz.isAnnotationPresent(Qualifier.class))
        .collect(Collectors.toList());
  }
}
