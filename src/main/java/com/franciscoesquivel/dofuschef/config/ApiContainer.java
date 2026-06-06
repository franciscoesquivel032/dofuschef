package com.franciscoesquivel.dofuschef.config;

import java.util.HashMap;
import java.util.Map;

public class ApiContainer {
    private final Map<Class<?> , Object> apis = new HashMap<>();

    public <T> void register(Class<T> type, T instance) {
        apis.put(type, instance);
    }

    public <T> T get(Class<T> type) {
        Object api = apis.get(type);
        if(api == null) {
            throw new IllegalArgumentException("API not registered: " + type.getName());
        }
        return type.cast(api);
    }
}
