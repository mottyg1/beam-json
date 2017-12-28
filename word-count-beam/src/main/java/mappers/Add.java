package mappers;

import ason.Ason;
import org.apache.beam.sdk.transforms.DoFn;


public class Add extends DoFn<Ason, Ason> {

    private final String key;
    private final Object value;

    public Add(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        Ason input = Utils.deepClone(c.element());
        input.put(this.key, this.value);
        c.output(input);
    }

}
