package conditions;

import java.io.Serializable;

public interface Predicate<T> extends Serializable {

    boolean test(T elem);

}
