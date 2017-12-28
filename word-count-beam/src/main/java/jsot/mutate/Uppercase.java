package jsot.mutate;

import jsot.core.Jsot;
import jsot.functional.MapFunction;

public class Uppercase {

    public static MapFunction<Jsot, Jsot> on(final String field) {
        return new MapFunction<Jsot, Jsot>() {
            @Override
            public Jsot apply(Jsot target) {
                target = target.deepClone();
                String value = (String) target.get(field);
                if (value != null) {
                    target.set(field, value.toUpperCase());
                }
                return target;
            }
        };
    }

}
