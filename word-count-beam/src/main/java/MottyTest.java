import ason.Ason;
import com.google.gson.Gson;
import conditions.Condition;
import jsot.core.Jsot;
import mappers.Mutate;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;


public class MottyTest {

    public static class StdoutPrintFn extends SimpleFunction<Ason, Ason> {
        @Override
        public Ason apply(Ason input) {
            System.out.println(new Gson().toJson(input.toStockJson().toMap()));
            return input;
        }
    }

    public static class FromJsonFn extends SimpleFunction<String, Ason> {
        @Override
        public Ason apply(String input) {
            return new Ason(input);
        }
    }


    public static void main(String[] args) throws Exception {

        Pipeline p = Pipeline.create();
        p.getCoderRegistry().registerCoderForClass(Jsot.class, new JsotCoder());
        p.getCoderRegistry().registerCoderForClass(Ason.class, new AsonCoder());

        p.apply("ReadLines", TextIO.read().from("/DATA/git/jsot/src/main/resources/o.json"))
                .apply(MapElements.via(new FromJsonFn()))
                .apply(Mutate.add("c", 5))
                .apply(Mutate.convert("c", String.class))
                .apply(Mutate.remove("b"))
                .apply(Mutate.rename("a", "new_a"))
                .apply(Mutate.uppercase("new_a"))
                .apply(
                        new If(Condition.has("new_a"))
                                .then(Mutate.add("if", true))
                                .otherwise(Mutate.add("if", false))
                )
                //  .apply(Mutate.add("d", new double[]{}))
                //  .apply(Mutate.rename("a", "newA"))
                //  .apply(MapElements.via(BeamMutateAdapter.uppercase("a"))
                //  .apply(MapElements.via(BeamMutateAdapter.rename("a", "newA")))
                //  .apply(MapElements.via(BeamMutateAdapter.rename("b", "newB")))
                .apply(MapElements.via(new StdoutPrintFn()))
        ;

        p.run().waitUntilFinish();
    }
}
