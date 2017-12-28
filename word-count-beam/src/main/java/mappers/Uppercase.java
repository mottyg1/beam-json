package mappers;

import ason.Ason;
import org.apache.beam.sdk.transforms.DoFn;


public class Uppercase extends DoFn<Ason, Ason> {

    private final String key;

    public Uppercase(String key) {
        this.key = key;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        Ason input = Utils.deepClone(c.element());

        String value = input.get(key);
        if (value != null) {
            input.put(this.key, value.toUpperCase());
        }

        c.output(input);
    }

}
