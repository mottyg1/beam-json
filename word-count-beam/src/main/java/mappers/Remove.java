package mappers;

import ason.Ason;
import org.apache.beam.sdk.transforms.DoFn;


public class Remove extends DoFn<Ason, Ason> {

    private final String key;

    public Remove(String key) {
        this.key = key;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        Ason input = Utils.deepClone(c.element());
        input.remove(this.key);
        c.output(input);
    }

}
