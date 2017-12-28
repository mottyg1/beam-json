package mappers;

import ason.Ason;

public class Utils {

    public static Ason deepClone(Ason original) {
        return new Ason(original.toString());
    }

}
