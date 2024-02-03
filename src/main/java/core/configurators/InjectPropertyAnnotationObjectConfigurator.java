package core.configurators;

import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

import core.ApplicationContext;
import core.annotations.InjectProperty;
import lombok.SneakyThrows;

public class InjectPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
  private static final String PROPERTY_FILE = "application.properties";

  private final Map<String, String> propertiesMap;

  @SneakyThrows
  public InjectPropertyAnnotationObjectConfigurator() {
    this.propertiesMap = parseProperties();
  }

  private Map<String, String> parseProperties() throws FileNotFoundException {
    URL resource = ClassLoader.getSystemClassLoader().getResource(PROPERTY_FILE);
    if (resource == null) throw new FileNotFoundException(PROPERTY_FILE);
    Stream<String> lines = new BufferedReader(new FileReader(resource.getPath())).lines();

    return lines
        .filter(line -> !line.startsWith("#"))
        .map(line -> line.split("="))
        .collect(toMap(arr -> arr[0].trim(), arr -> arr[1].trim()));
  }

  @Override
  @SneakyThrows
  public void configure(Object t, ApplicationContext context) {
    Class<?> implClass = t.getClass();
    for (Field field : implClass.getDeclaredFields()) {
      InjectProperty annotation = field.getAnnotation(InjectProperty.class);
      if (annotation != null) {
        injectType(t, field, annotation);
      }
    }
  }

  private void injectType(Object t, Field field, InjectProperty annotation)
      throws IllegalAccessException, RuntimeException {
    String value =
        annotation.value().isEmpty()
            ? propertiesMap.get(field.getName())
            : propertiesMap.get(annotation.value());

    if (value == null) {
      throw new RuntimeException(
          String.format(
              "Can't configurate object: %s - field `%s`, injected value is null",
              t.getClass().getName(), field.getName()));
    }

    field.setAccessible(true);
    field.set(t, value);
  }
}
