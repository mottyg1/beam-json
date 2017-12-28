package mappers;

import ason.Ason;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;


// A more convenient class to use
// TODO: Maybe add a cache?
public class Mutate {

    public static PTransform<PCollection<Ason>, PCollection<Ason>> add(final String key, final Object value) {
        return new PTransform<PCollection<Ason>, PCollection<Ason>>() {
            @Override
            public PCollection<Ason> expand(PCollection<Ason> asonPCollection) {
                return asonPCollection.apply(ParDo.of(new Add(key, value)));
            }
        };
    }

    public static ParDo.SingleOutput<Ason, Ason> convert(final String key, final Class newType) throws Exception {
        return ParDo.of(new Convert(key, newType));
    }

    public static ParDo.SingleOutput<Ason, Ason> remove(final String key) {
        return ParDo.of(new Remove(key));
    }

    public static ParDo.SingleOutput<Ason, Ason> rename(final String key, final String newKey) {
        return ParDo.of(new Rename(key, newKey));
    }

    public static ParDo.SingleOutput<Ason, Ason> uppercase(final String key) {
        return ParDo.of(new Uppercase(key));
    }

}

