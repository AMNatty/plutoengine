package cz.tefek.l10n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;

public class PlutoL10n
{
    private static Locale defaultLocale;

    private static Map<Locale, Map<String, String>> localizations = new HashMap<>();

    public static void init(Locale locale)
    {
        Locale.setDefault(locale);
        defaultLocale = locale;

        setLocale(defaultLocale);

        Logger.logf(SmartSeverity.INFO, "Default locale: %s\n", defaultLocale.toString());
    }

    public static void registerLocale(Locale locale)
    {
        if (localizations.containsKey(locale))
        {
            return;
        }

        localizations.put(locale, new HashMap<>());
    }

    public static void setLocale(Locale locale)
    {
        registerLocale(locale);
        Locale.setDefault(locale);
    }

    public static void map(Locale locale, String src, String dest)
    {
        registerLocale(locale);
        localizations.get(locale).put(src, dest);
    }

    public static String get(Locale locale, String src)
    {
        if (!localizations.containsKey(locale))
        {
            if (!localizations.containsKey(defaultLocale))
            {
                return src;
            }

            return localizations.get(defaultLocale).getOrDefault(src, src);
        }

        return localizations.get(locale).getOrDefault(src, src);
    }

    public static String get(String src)
    {
        return get(Locale.getDefault(), src);
    }
}
