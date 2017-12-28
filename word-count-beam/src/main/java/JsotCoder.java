import jsot.core.Jsot;
import jsot.core.JsotImpl;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

public class JsotCoder extends Coder<Jsot> {
    @Override
    public void encode(Jsot jsot, OutputStream outputStream) throws CoderException, IOException {
        outputStream.write(jsot.encode());
    }

    @Override
    public Jsot decode(InputStream inputStream) throws CoderException, IOException {
        return new JsotImpl(IOUtils.toByteArray(inputStream));
    }

    @Override
    public List<? extends Coder<?>> getCoderArguments() {
        return null;
    }

    @Override
    public void verifyDeterministic() throws NonDeterministicException {

    }
}
