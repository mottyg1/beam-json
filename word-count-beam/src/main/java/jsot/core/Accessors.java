package jsot.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// modified from logstash

public final class Accessors {

    private Accessors() {
        //Utility Class
    }

    public static Object get(final Map data, final FieldReference field) {
        final Object target = findParent(data, field);
        return target == null ? null : fetch(target, field.getKey());
    }

    public static Object set(final Map data, final FieldReference field,
                             final Object value) {
        return setChild(findCreateTarget(data, field), field.getKey(), value);
    }

    public static Object del(final Map data, final FieldReference field) {
        final Object target = findParent(data, field);
        if (target instanceof Map) {
            return ((Map) target).remove(field.getKey());
        } else {
            return target == null ? null : delFromList((List) target, field.getKey());
        }
    }

    public static boolean includes(final Map data, final FieldReference field) {
        final Object target = findParent(data, field);
        final String key = field.getKey();
        return target instanceof Map && ((Map) target).containsKey(key) ||
                target instanceof List && foundInList(key, (List) target);
    }

    private static Object delFromList(final List list, final String key) {
        try {
            return list.remove(listIndex(key, list.size()));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return null;
        }
    }

    private static Object setOnList(final String key, final Object value, final List list) {
        final int index;
        try {
            index = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            return null;
        }
        final int size = list.size();
        if (index >= size) {
            appendAtIndex(list, value, index, size);
        } else {
            list.set(listIndex(index, size), value);
        }
        return value;
    }

    private static void appendAtIndex(final List list, final Object value, final int index,
                                      final int size) {
        // grow array by adding trailing null items
        // this strategy reflects legacy Ruby impl behaviour and is backed by specs
        // TODO: (colin) this is potentially dangerous, and could produce OOM using arbitrary big numbers
        // TODO: (colin) should be guard against this?
        for (int i = size; i < index; i++) {
            list.add(null);
        }
        list.add(value);
    }

    private static Object findParent(final Map data, final FieldReference field) {
        Object target = data;
        for (final String key : field.getPath()) {
            target = fetch(target, key);
            if (!(target instanceof Map || target instanceof List)) {
                return null;
            }
        }
        return target;
    }

    private static Object findCreateTarget(final Map data, final FieldReference field) {
        Object target = data;
        boolean create = false;
        for (final String key : field.getPath()) {
            Object result;
            if (create) {
                result = createChild((Map) target, key);
            } else {
                result = fetch(target, key);
                create = result == null;
                if (create) {
                    result = new HashMap<>(1);
                    setChild(target, key, result);
                }
            }
            target = result;
        }
        return target;
    }

    private static Object setChild(final Object target, final String key, final Object value) {
        if (target instanceof Map) {
            ((Map) target).put(key, value);
            return value;
        } else {
            return setOnList(key, value, (List) target);
        }
    }

    private static Object createChild(final Map target, final String key) {
        final Object result = new HashMap<>(1);
        target.put(key, result);
        return result;
    }

    private static Object fetch(Object target, String key) {
        return target instanceof Map
                ? ((Map) target).get(key) : fetchFromList((List) target, key);
    }

    private static Object fetchFromList(final List list, final String key) {
        try {
            return list.get(listIndex(key, list.size()));
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return null;
        }
    }

    private static boolean foundInList(final String key, final List target) {
        return fetchFromList(target, key) != null;
    }

    /**
     * Returns a positive integer offset from a Ruby style positive or negative list index.
     *
     * @param i    List index
     * @param size the size of the list
     * @return the positive integer offset for the list given by index i
     */
    public static int listIndex(int i, int size) {
        return i < 0 ? size + i : i;
    }

    /**
     * Returns a positive integer offset for a list of known size.
     *
     * @param key  List index (String matching /[0-9]+/)
     * @param size the size of the list
     * @return the positive integer offset for the list given by index i
     */
    private static int listIndex(final String key, final int size) {
        return listIndex(Integer.parseInt(key), size);
    }
}
