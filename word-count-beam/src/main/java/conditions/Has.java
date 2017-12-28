package conditions;

import ason.Ason;


public class Has implements Predicate<Ason> {

    private final String key;

    public Has(String key) {
        this.key = key;
    }

    public boolean test(Ason ason) {
        return ason.has(key);
    }
}
