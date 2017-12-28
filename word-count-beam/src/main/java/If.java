import ason.Ason;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.Partition;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;


public class If extends PTransform<PCollection<Ason>, PCollection<Ason>> {

    private final Partition<Ason> condition;
    private PTransform<PCollection<Ason>, PCollection<Ason>> ifTrueTransform;
    private PTransform<PCollection<Ason>, PCollection<Ason>> ifFalseTransform;

    public If(final Partition<Ason> condition) {
        this.condition = condition;
    }

    public If then(PTransform<PCollection<Ason>, PCollection<Ason>> ifTrueTransform) {
        this.ifTrueTransform = ifTrueTransform;
        return this;
    }

    public If otherwise(PTransform<PCollection<Ason>, PCollection<Ason>> ifFalseTransform) {
        this.ifFalseTransform = ifFalseTransform;
        return this;
    }

    @Override
    public PCollection<Ason> expand(PCollection<Ason> asonPCollection) {
        PCollectionList<Ason> trueAndFalse = asonPCollection.apply(condition);

        PCollection<Ason> trueTransformed = trueAndFalse.get(1).apply(this.ifTrueTransform);
        PCollection<Ason> falseTransformed = trueAndFalse.get(0).apply(this.ifFalseTransform);

        PCollectionList<Ason> trueAndFalseCombined = PCollectionList.of(trueTransformed).and(falseTransformed);

        return trueAndFalseCombined.apply(Flatten.<Ason>pCollections());
    }
}
