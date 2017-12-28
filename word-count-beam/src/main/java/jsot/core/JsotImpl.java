package jsot.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsotImpl implements Jsot, Serializable {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final ObjectMapper CBOR_MAPPER = new ObjectMapper(new CBORFactory().configure(CBORGenerator.Feature.WRITE_MINIMAL_INTS, false))
            .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);


    private final Map<String, Object> data;

    public JsotImpl() {
        this.data = new HashMap<>();
    }

    public JsotImpl(Map<String, Object> data) {
        this.data = data;
    }

    public JsotImpl(String json) {
        this.data = GSON.fromJson(json, Map.class);
    }

    public JsotImpl(byte[] serial) {
       // this.data = CBOR_MAPPER.readValue(serial, Map.class);
        this.data = GSON.fromJson(new String(serial), Map.class);
    }

    @Override
    public Object get(String field) {
        return Accessors.get(this.data, FieldReference.from(field));
    }

    @Override
    public void set(String field, Object new_value) {
        Accessors.set(this.data, FieldReference.from(field), new_value);
    }

    @Override
    public Object del(String field) {
        return Accessors.del(this.data, FieldReference.from(field));
    }

    @Override
    public String toString() {
        return GSON.toJson(this.data);
    }

    @Override
    public byte[] encode() {
        return this.toString().getBytes();
       // return CBOR_MAPPER.writeValueAsBytes(this.data);
    }

    @Override
    public Jsot deepClone() {
        return new JsotImpl(this.encode());
    }

}
