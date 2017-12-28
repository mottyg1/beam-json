package jsot.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

// modified from logstash

public final class FieldReference {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\.|\\[|\\]");

    private static final Map<CharSequence, FieldReference> CACHE = new ConcurrentHashMap<>(64, 0.2F, 1);

    private final String[] path;

    private final String key;

    private FieldReference(final String[] path, final String key) {
        this.key = key;
        this.path = path;
    }

    public static FieldReference from(final CharSequence reference) {
        // atomicity between the get and put is not important
        final FieldReference result = CACHE.get(reference);
        if (result != null) {
            return result;
        }
        return parseToCache(reference);
    }

    public String[] getPath() {
        return path;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (!(that instanceof FieldReference)) return false;
        final FieldReference other = (FieldReference) that;
        return key.equals(other.key) && Arrays.equals(path, other.path);
    }

    private static FieldReference parseToCache(final CharSequence reference) {
        final FieldReference result = parse(reference);
        CACHE.put(reference, result);
        return result;
    }

    private static FieldReference parse(final CharSequence reference) {
        final String[] parts = SPLIT_PATTERN.split(reference);
        final List<String> path = new ArrayList<>(parts.length);
        for (final String part : parts) {
            if (!part.isEmpty()) {
                path.add(part.intern());
            }
        }
        final String key = path.remove(path.size() - 1).intern();

        return new FieldReference(path.toArray(new String[]{}), key);
    }
}
