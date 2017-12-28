import ason.Ason;
import jsot.core.JsotImpl;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class AsonCoder extends Coder<Ason> {
    @Override
    public void encode(Ason ason, OutputStream outputStream) throws CoderException, IOException {
        outputStream.write(ason.toString().getBytes());
    }

    @Override
    public Ason decode(InputStream inputStream) throws CoderException, IOException {
        return new Ason(new String(IOUtils.toByteArray(inputStream)));
    }

    @Override
    public List<? extends Coder<?>> getCoderArguments() {
        return null;
    }

    @Override
    public void verifyDeterministic() throws NonDeterministicException {

    }
}
