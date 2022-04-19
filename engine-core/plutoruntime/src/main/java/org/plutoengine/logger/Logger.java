package org.plutoengine.logger;

import org.intellij.lang.annotations.PrintFormat;
import org.plutoengine.component.ComponentToken;
import org.plutoengine.component.PlutoGlobalComponent;
import org.plutoengine.resource.filesystem.ResourceManager;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * <p>
 * A simple static logger writing to both standard output and a log file.
 * </p>
 *
 * <p>
 * The log file is currently hardcoded to {@link ResourceManager#GLOBAL_ROOT}/<code>logs/log--YYYY-MM-DD--HH-MM-SS.txt</code>
 * </p>
 *
 * @author 493msi
 *
 * @since pre-alpha
 */
public class Logger extends PlutoGlobalComponent
{
    public static final ComponentToken<Logger> TOKEN = ComponentToken.create(Logger::new);

    private static PrintStream stdout = null;
    private static PrintStream stderr = null;

    private static OutputStream fileLog = null;

    private Logger()
    {

    }

    /**
     * Initializes the logger and replaces the standard output and standard error output with the logger methods.
     *
     * <p>
     * <em>
     * This method will close the logger first (if it is already open).
     * </em>
     * </p>
     *
     * @since pre-alpha
     * */
    @Override
    protected void onMount(ComponentDependencyManager manager) throws Exception
    {
        this.onUnmount();

        stdout = new PrintStream(System.out);
        stderr = new PrintStream(System.err);

        // TODO: Unhardcode the log directory

        var logsDir = ResourceManager.GLOBAL_ROOT.resolve("logs");

        if (!Files.isDirectory(logsDir))
        {
            if (Files.exists(logsDir))
            {
                System.err.printf("[#] Failed to initialize the logger, the path `%s` is obstructed by a non-directory!%n", logsDir.toAbsolutePath());
                return;
            }

            Files.createDirectories(logsDir);
        }

        var dateTime = LocalDateTime.now();
        var logName = String.format("log--%04d-%02d-%02d--%02d-%02d-%02d.txt",
                dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());

        fileLog = Files.newOutputStream(logsDir.resolve(logName));

        System.setOut(new PrintStream(new OutputSplitStream(stdout, false, fileLog, false)));
        System.setErr(new PrintStream(new OutputSplitStream(stderr, false, fileLog, false)));
    }

    /**
     * Closes the logger and its respective log file, if there is one.
     *
     * <p>
     * <em>
     * This method is a noop for an already closed logger.
     * </em>
     * </p>
     *
     * @since pre-alpha
     * */
    @Override
    protected void onUnmount() throws Exception
    {
        if (fileLog != null)
            fileLog.close();
        fileLog = null;

        if (stdout != null)
            System.setOut(stdout);
        stdout = null;

        if (stderr != null)
            System.setErr(stderr);
        stderr = null;
    }

    /**
     * Logs an object converted to a {@link String} to the stdout stream.
     *
     * <em>
     * Appends a newline.
     * </em>
     *
     * @param string The object convertible to a {@link String}
     *
     * @since pre-alpha
     * */
    public static synchronized void log(Object string)
    {
        System.out.println(string);
    }

    /**
     * Logs a printf-style message to the stdout stream.
     *
     * <em>
     * No newline is appended.
     * </em>
     *
     * <em>
     * Appends a newline.
     * </em>
     *
     * @param formatSpec The format specifier {@link String}
     * @param o A list of input objects
     *
     * @since pre-alpha
     * */
    public static synchronized void logf(String formatSpec, Object... o)
    {
        System.out.printf(formatSpec, o);
    }

    /**
     * Logs an object converted to a {@link String} to the stdout stream <em>without appending a newline</em>.
     *
     * @param string The object convertible to a {@link String}
     *
     * @since pre-alpha
     * */
    public static synchronized void logNoLine(Object string)
    {
        System.out.print(string);
    }

    /**
     * Logs the stack trace of a {@link Throwable} to the stderr stream.
     *
     * @param e The throwable to log
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static synchronized void log(Throwable e)
    {
        e.printStackTrace();
    }

    /**
     * Logs an empty message with the specified severity. That's it.
     *
     * <em>
     * A newline is automatically appended.
     * </em>
     *
     * @param s The severity of the logged message
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static synchronized void log(ISeverity s)
    {
        (s.isStdErr() ? System.err : System.out).println(s.getDisplayName());
    }

    /**
     * Logs an object converted to a {@link String} with the specified severity.
     *
     * <em>
     * A newline is automatically appended.
     * </em>
     *
     * @param s The severity of the logged message
     * @param string The object convertible to a {@link String}
     *
     * @since pre-alpha
     * */
    public static synchronized void log(ISeverity s, Object string)
    {
        (s.isStdErr() ? System.err : System.out).println(s.getDisplayName() + string);
    }

    /**
     * Logs a printf-style message with the specified severity.
     *
     * <em>
     * No newline is appended.
     * </em>
     *
     * @param s The severity of the logged message
     * @param formatSpec The format specifier {@link String}
     * @param o A list of input objects
     *
     * @since pre-alpha
     * */
    public static synchronized void logf(ISeverity s, @PrintFormat String formatSpec, Object... o)
    {
        (s.isStdErr() ? System.err : System.out).printf(s.getDisplayName() + formatSpec, o);
    }

    /**
     * Logs an object converted to a {@link String} with the specified severity <em>without appending a newline</em>.
     *
     * @param s The severity of the logged message
     * @param string The object convertable to a {@link String}
     *
     * @since pre-alpha
     * */
    public static synchronized void logNoLine(ISeverity s, Object string)
    {
        (s.isStdErr() ? System.err : System.out).print(s.getDisplayName() + string);
    }

    @Override
    public boolean isUnique()
    {
        return true;
    }
}
