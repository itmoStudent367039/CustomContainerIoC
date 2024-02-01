package core.configurators;

public interface ProxyConfigurator {
    Object replaceWithProxyIfNeeded(Object t, Class<?> implClass);
}
