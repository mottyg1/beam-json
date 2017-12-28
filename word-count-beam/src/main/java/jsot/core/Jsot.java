package jsot.core;

public interface Jsot {
    Object get(String field);

    void set(String field, Object new_value);

    Object del(String field);

    @Override
    String toString();

    byte[] encode();

    Jsot deepClone();

}