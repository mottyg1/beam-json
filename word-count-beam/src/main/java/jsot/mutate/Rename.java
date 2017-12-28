package jsot.mutate;

import jsot.core.Jsot;
import jsot.functional.MapFunction;

public class Rename {

    public static MapFunction<Jsot, Jsot> on(final String field, final String newName) {
        return new MapFunction<Jsot, Jsot>() {
            @Override
            public Jsot apply(Jsot target) {
                target = target.deepClone();
                Object value = target.del(field);
                target.set(newName, value);
                return target;
            }
        };
    }

}
