package cz.tefek.io.pluto.debug;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Splits one {@link OutputStream} into two to allow writing to both log file
 * and console output.
 *
 * @author 493msi
 *
 */
class StdOutSplitStream extends OutputStream
{
    public StdOutSplitStream()
    {

    }

    @Override
    public void write(int b) throws IOException
    {
        Logger.file_log.write(b);
        Logger.stdout.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        Logger.file_log.write(b);
        Logger.stdout.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        Logger.file_log.write(b, off, len);
        Logger.stdout.write(b, off, len);
    }

    @Override
    public void flush() throws IOException
    {
        Logger.file_log.flush();
        Logger.stdout.flush();
    }

    @Override
    public void close() throws IOException
    {
        Logger.file_log.close();
    }
}
