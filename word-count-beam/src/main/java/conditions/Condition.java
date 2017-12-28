package conditions;

import ason.Ason;
import org.apache.beam.sdk.transforms.Partition;


public class Condition {

//    public static Partition<Ason> of(final Predicate<Ason> predicate) {
//        return Partition.of(2, new ConPar(predicate));
//    }

    public static Partition<Ason> has(final String key) {
        return Partition.of(2, new Partition.PartitionFn<Ason>() {
            @Override
            public int partitionFor(Ason elem, int numPartitions) {
                return new Has(key).test(elem) ? 1 : 0;
            }
        });
    }

}

