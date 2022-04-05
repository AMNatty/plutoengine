package org.plutoengine.logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Splits one {@link OutputStream} into two to allow writing to both log file
 * and console output.
 *
 * @author 493msi
 *
 */
public class OutputSplitStream extends OutputStream
{
    private final OutputStream outputStreamA;
    private final boolean shouldAClose;

    private final OutputStream outputStreamB;
    private final boolean shouldBClose;

    public OutputSplitStream(OutputStream outputStreamA, boolean shouldAClose,
                             OutputStream outputStreamB, boolean shouldBClose)
    {
        this.outputStreamA = outputStreamA;
        this.shouldAClose = shouldAClose;

        this.outputStreamB = outputStreamB;
        this.shouldBClose = shouldBClose;
    }

    @Override
    public void write(int b) throws IOException
    {
        this.outputStreamA.write(b);
        this.outputStreamB.write(b);
    }

    @Override
    public void write(@Nonnull byte[] b) throws IOException
    {
        this.outputStreamA.write(b);
        this.outputStreamB.write(b);
    }

    @Override
    public void write(@Nonnull byte[] b, int off, int len) throws IOException
    {
        this.outputStreamA.write(b, off, len);
        this.outputStreamB.write(b, off, len);
    }

    @Override
    public void flush() throws IOException
    {
        this.outputStreamA.flush();
        this.outputStreamB.flush();
    }

    @Override
    public void close() throws IOException
    {
        if (this.shouldAClose)
            outputStreamA.close();

        if (this.shouldBClose)
            outputStreamB.close();
    }
}
