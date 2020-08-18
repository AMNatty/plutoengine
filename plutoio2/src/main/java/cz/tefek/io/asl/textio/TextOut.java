package cz.tefek.io.asl.textio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Simplifies text writer creation. For reading use {@link TextIn}.
 *
 * @author 493msi
 */

public class TextOut
{
    public static PrintStream createPrintStream(String filePath) throws IOException
    {
        PrintStream printstream = new PrintStream(createFOStream(filePath));

        return printstream;
    }

    public static FileOutputStream createFOStream(String filePath) throws IOException
    {
        FileOutputStream fos = new FileOutputStream(filePath);

        return fos;
    }
}
