package com.thoughtworks.lean.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MapUtil {
    @SuppressWarnings("unchecked")
    public static <T> T get(Map map, String key) {
        Object o = map.get(key);
        if (o == null) {
            return null;
        }
        return (T) o;
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getOptioal(Map map, String key) {
        Object o = map.get(key);
        if (o == null) {
            return null;
        }
        return Optional.of((T) o);
    }

    @SuppressWarnings("unchecked")
    public static <T extends List> T getList(Map map, String key) {
        Object o = map.get(key);
        if (o == null) {
            return null;
        }
        return (T) o;
    }
}
