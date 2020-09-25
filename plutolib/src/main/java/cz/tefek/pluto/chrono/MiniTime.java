package cz.tefek.pluto.chrono;

import java.util.concurrent.TimeUnit;

/**
 * A helper class to convert from a time span in milliseconds to a simplified
 * time span {@link String} format and vice versa. Note this action is fully
 * reversible at the cost of losing millisecond precision.
 * 
 * <h3>MiniTime format specification:</h3>
 * 
 * <pre>
 * [Nw][Nd][Nh][Nm][Ns]
 * 
 *  w - weeks
 *  d - days
 *  h - hours
 *  m - minutes
 *  s - seconds
 *  
 *  N - a decimal integer
 * </pre>
 * <ul>
 * <li>At least one value is required.</li>
 * <li>At least time unit is required.</li>
 * <li>Time units are case insensitive.</li>
 * <li>String cannot start with a letter or end with a number.</li>
 * <li>All values must be valid <i>positive</i> 32-bit signed integers.</li>
 * <li>All values except the first one must be less than 1 of the previous time
 * unit.</li>
 * <li>Skipping a time unit assumes zero of that time unit.</li>
 * </ul>
 * 
 * <h3>Permitted values:</h3>
 * <ul>
 * <li>standard MiniTime scheme, from <code>0s</code> to
 * <code>2147483647w6d23h59m59s</code></li>
 * <li>string <code>"forever"</code> (parses as {@link Long#MAX_VALUE})</li>
 * </ul>
 * 
 * @author 493msi
 * @since 0.2
 */
public class MiniTime
{
    private static class MiniTimeParseException extends RuntimeException
    {
        /**
         * 
         */
        private static final long serialVersionUID = -5403949842120041373L;

        public MiniTimeParseException()
        {
            super("Time period could not be parsed. Correct format: \\_w\\_d\\_h\\_m\\_s **without spaces** between the units. You can skip a time unit. Example: 1h15m");
        }
    }

    private static final TimeUnit miliseconds = TimeUnit.MILLISECONDS;

    private static final int DAYS_IN_WEEK = 7;
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLIS_IN_MINUTE = 1000;

    /**
     * Converts a MiniTime spec string to the respective time duration in
     * milliseconds.
     * 
     * @param input The source MiniTime non-null string
     * @return The resulting time span in milliseconds
     * 
     * @author 493msi
     * @since 0.2
     */
    public static long parse(String input)
    {
        if (input == null)
        {
            throw new IllegalArgumentException("MiniTime string cannot be null!");
        }

        // Nothing to parse
        if (input.isEmpty())
        {
            throw new MiniTimeParseException();
        }

        if (input.equalsIgnoreCase("forever"))
        {
            return Long.MAX_VALUE;
        }

        // Follow the scheme
        if (!input.matches("[0-9]*[wW]?[0-9]*[dD]?[0-9]*[hH]?[0-9]*[mM]?[0-9]*[sS]?"))
        {
            throw new MiniTimeParseException();
        }

        // 4584 of what? Potatoes?
        if (input.matches("[0-9]+"))
        {
            throw new MiniTimeParseException();
        }

        // Where are the numbers?
        if (input.matches("[a-zA-Z]+"))
        {
            throw new MiniTimeParseException();
        }

        // It shouldn't start with a letter
        if (input.matches("^[a-zA-Z].+"))
        {
            throw new MiniTimeParseException();
        }

        var nrs = input.split("[a-zA-Z]");
        var letters = input.split("[0-9]+");

        if (nrs.length != letters.length)
        {
            throw new MiniTimeParseException();
        }

        long time = 0;

        for (int i = 1; i < nrs.length; i++)
        {
            var type = letters[i - 1];
            int number;

            try
            {
                // The only time this fails is when the number is too long
                number = Integer.parseUnsignedInt(nrs[i]);
            }
            catch (NumberFormatException nfe)
            {
                throw new MiniTimeParseException();
            }

            var allow = 0L;
            var multiplier = 0;

            switch (type.toLowerCase())
            {
                case "w":
                case "W":
                    allow = Integer.MAX_VALUE;
                    multiplier = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_WEEK * MILLIS_IN_MINUTE;
                    break;
                case "d":
                case "D":
                    allow = DAYS_IN_WEEK;
                    multiplier = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * MILLIS_IN_MINUTE;
                    break;
                case "h":
                case "H":
                    allow = HOURS_IN_DAY;
                    multiplier = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * MILLIS_IN_MINUTE;
                    break;
                case "m":
                case "M":
                    allow = MINUTES_IN_HOUR;
                    multiplier = SECONDS_IN_MINUTE * MILLIS_IN_MINUTE;
                    break;
                case "s":
                case "S":
                    allow = SECONDS_IN_MINUTE;
                    multiplier = MILLIS_IN_MINUTE;
                    break;
                default:
                    break;
            }

            // The top one can be more than it normally could have, for example you can
            // issue a ban for 48h but not 46h120m (it looks dumb)
            if (i == 1)
            {
                allow = Integer.MAX_VALUE;
            }

            if (number > allow)
            {
                throw new MiniTimeParseException();
            }

            time += multiplier * number;
        }

        return System.currentTimeMillis() + time;
    }

    /**
     * Converts a time span between two Unix-epoch millisecond time points to a
     * MiniTime string. <i>Note ALL time spans larger or equal than
     * {@link Integer#MAX_VALUE} weeks will be permanently converted to "forever".  
     * Inputting {@link Long#MAX_VALUE} for the future time point has the same effect.</i>
     * 
     * @param before The first time point in Unix-time milliseconds
     * @param after  The first time point in Unix-time milliseconds
     *
     * @return The resulting MiniTime string
     * 
     * @throws IllegalArgumentException on a negative time duration
     * 
     * @author 493msi
     * @since 0.2
     */
    public static String fromMillisDiffNow(long after, long before)
    {
        if (after == Long.MAX_VALUE)
        {
            return "forever";
        }

        return formatDiff(after - before);
    }

    /**
     * Converts a time span between now and a future time point in Unix-epoch
     * milliseconds to a MiniTime string. <i>Note ALL time spans larger or equal
     * than {@link Integer#MAX_VALUE} weeks will be permanently converted to
     * "forever". Inputting {@link Long#MAX_VALUE} for the future time point
     * has the same effect.</i>
     * 
     * @param future The future time point in Unix time milliseconds
     *
     * @return The resulting MiniTime string
     * 
     * @throws IllegalArgumentException on a negative time duration
     * 
     * @author 493msi
     * @since 0.2
     */
    public static String fromMillisDiffNow(long future)
    {
        if (future == Long.MAX_VALUE)
        {
            return "forever";
        }

        var diff = future - System.currentTimeMillis();

        return formatDiff(diff);
    }

    /**
     * Converts a time span milliseconds to a MiniTime string. <i>Note ALL time
     * spans larger or equal than {@link Integer#MAX_VALUE} weeks will be permanently
     * converted to "forever".</i>
     * 
     * @param diff The source time span in milliseconds
     * @return The resulting MiniTime string
     * 
     * @throws IllegalArgumentException on a negative time duration
     * 
     * @author 493msi
     * @since 0.2
     */
    public static String formatDiff(long diff)
    {
        if (diff < 0)
        {
            throw new IllegalArgumentException("Negative time span cannot be converted to MiniTime.");
        }

        var xweeks = miliseconds.toDays(diff) / DAYS_IN_WEEK;

        if (xweeks > Integer.MAX_VALUE)
        {
            return "forever";
        }

        var xdays = miliseconds.toDays(diff) % DAYS_IN_WEEK;
        var xhours = miliseconds.toHours(diff) % HOURS_IN_DAY;
        var xminutes = miliseconds.toMinutes(diff) % MINUTES_IN_HOUR;
        var xseconds = miliseconds.toSeconds(diff) % SECONDS_IN_MINUTE;

        return formatTime(xweeks, xdays, xhours, xminutes, xseconds);
    }

    private static String formatTime(long weeks, long days, long hours, long minutes, long seconds)
    {
        var sb = new StringBuilder();

        if (weeks > 0)
        {
            sb.append(weeks);
            sb.append('w');
        }
        if (days > 0)
        {
            sb.append(days);
            sb.append('d');
        }
        if (hours > 0)
        {
            sb.append(hours);
            sb.append('h');
        }
        if (minutes > 0)
        {
            sb.append(minutes);
            sb.append('m');
        }
        if (seconds > 0)
        {
            sb.append(seconds);
            sb.append('s');
        }

        var timeStr = sb.toString();

        return timeStr.isEmpty() ? "0s" : timeStr;
    }
}
