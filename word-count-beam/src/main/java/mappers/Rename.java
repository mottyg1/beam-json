package mappers;

import ason.Ason;
import org.apache.beam.sdk.transforms.DoFn;


public class Rename extends DoFn<Ason, Ason> {

    private final String key;
    private final String newKey;

    public Rename(String key, String newKey) {
        this.key = key;
        this.newKey = newKey;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        Ason input = Utils.deepClone(c.element());

        Object value = input.get(key);
        if (value != null) {
            input.remove(key).put(newKey, value);
        }

        c.output(input);
    }

}
