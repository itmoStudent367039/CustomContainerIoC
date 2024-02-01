package core.configurators;

import java.lang.reflect.Field;

import core.ApplicationContext;
import core.annotations.InjectByType;
import lombok.SneakyThrows;

public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {
  @Override
  @SneakyThrows
  public void configure(Object t, ApplicationContext context) {
    for (Field field : t.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(InjectByType.class)) {
        field.setAccessible(true);
        Object object = context.getObject(field.getType());
        field.set(t, object);
      }
    }
  }
}
