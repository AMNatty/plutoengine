package cz.tefek.io.pluto.debug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import cz.tefek.io.asl.resource.ResourceHelper;
import cz.tefek.io.asl.textio.TextOut;

/**
 * Logger. 'nuff said.
 *
 * @author 493msi
 *
 */
public class Logger
{
    static OutputStream stdout;
    static OutputStream stderr;

    static FileOutputStream file_log;

    static PrintStream stdoutStream;
    static PrintStream stderrStream;

    public static void setup()
    {
        stdout = new PrintStream(System.out);
        stderr = new PrintStream(System.err);

        setupFileStream();

        stdoutStream = new PrintStream(new StdOutSplitStream());
        stderrStream = new PrintStream(new StdErrSplitStream());

        System.setOut(stdoutStream);
        System.setErr(stderrStream);
    }

    private static void setupFileStream()
    {
        try
        {
            if (!new File(ResourceHelper.GLOBAL_ROOT + "logs").exists() || !new File(ResourceHelper.GLOBAL_ROOT + "logs").isDirectory())
            {
                new File(ResourceHelper.GLOBAL_ROOT + "logs").mkdirs();
            }

            file_log = TextOut.createFOStream(ResourceHelper.GLOBAL_ROOT + "logs/log" + System.currentTimeMillis() + ".txt");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void close()
    {
        System.out.close();
    }

    public static void log(Object string)
    {
        log(Severity.NONE, string);
    }

    public static void logf(String string, Object... o)
    {
        System.out.printf(string, o);
    }

    public static void logNoLine(Object string)
    {
        System.out.print(string);
    }

    public static void logException(Exception e)
    {
        e.printStackTrace();
    }

    public static void log(ISeverity s, Object string)
    {
        (s.isStdErr() ? System.err : System.out).println(s.getDisplayName() + string);
    }

    public static void logf(ISeverity s, String string, Object... o)
    {
        (s.isStdErr() ? System.err : System.out).printf(s.getDisplayName() + string, o);
    }

    public static void logNoLine(ISeverity s, Object string)
    {
        (s.isStdErr() ? System.err : System.out).print(s.getDisplayName() + string);
    }
}
