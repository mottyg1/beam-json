package jsot.mutate;

import jsot.core.Jsot;
import org.apache.beam.sdk.transforms.SimpleFunction;

public class BeamMutateAdapter {
    public static SimpleFunction<Jsot, Jsot> rename(final String field, final String newName) {
        return new SimpleFunction<Jsot, Jsot>() {
            @Override
            public Jsot apply(Jsot input) {
                return Mutate.rename(field, newName).apply(input);
            }
        };
    }

    public static SimpleFunction<Jsot, Jsot> uppercase(final String field) {
        return new SimpleFunction<Jsot, Jsot>() {
            @Override
            public Jsot apply(Jsot input) {
                return Mutate.uppercase(field).apply(input);
            }
        };
    }
}
