package conditions;

import ason.Ason;


public class ConPar implements org.apache.beam.sdk.transforms.Partition.PartitionFn<Ason> {

    private final Has predicate;

    public ConPar(Has predicate) {
        this.predicate = predicate;
    }

    @Override
    public int partitionFor(Ason elem, int numPartitions) {
        return predicate.test(elem) ? 1 : 0;
    }
}