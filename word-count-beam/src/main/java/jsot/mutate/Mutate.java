package jsot.mutate;

import jsot.core.Jsot;
import jsot.functional.MapFunction;

public class Mutate {

    public static MapFunction<Jsot, Jsot> rename(String field, String newName) {
        return Rename.on(field, newName);
    }

    public static MapFunction<Jsot, Jsot> uppercase(String field) {
        return Uppercase.on(field);
    }

}
