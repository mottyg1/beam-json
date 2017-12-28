package mappers;

import ason.Ason;
import org.apache.beam.sdk.transforms.DoFn;


public class Convert extends DoFn<Ason, Ason> {

    private final String key;
    private final Class newType;

    public Convert(String key, Class newType) throws Exception {
        this.key = key;
        if (newType != Integer.TYPE && newType != Double.TYPE && newType != String.class) {
            throw new Exception("cant convert to " + newType.getName());
        }
        this.newType = newType;
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        Ason input = Utils.deepClone(c.element());

        Object value = input.get(key);
        if (value != null) {
            if (newType == Integer.TYPE) {
                value = Integer.valueOf(value.toString());
            } else if (newType == Double.TYPE) {
                value = Double.valueOf(value.toString());
            } else if (newType == String.class) {
                value = String.valueOf(value);
            }

            input.put(key, value);
        }

        c.output(input);
    }

}
