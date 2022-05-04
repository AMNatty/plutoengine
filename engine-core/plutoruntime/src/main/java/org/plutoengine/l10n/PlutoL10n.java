/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.plutoengine.l10n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

/**
 * A localization manager. Extremely tentative.
 * */
public class PlutoL10n
{
    private static Locale defaultLocale;

    private static final Map<Locale, Map<String, String>> localizations = new HashMap<>();

    public static void init(Locale locale)
    {
        Locale.setDefault(locale);
        defaultLocale = locale;

        setLocale(defaultLocale);

        Logger.logf(SmartSeverity.INFO, "Default locale: %s\n", defaultLocale.toString());
    }

    public static void registerLocale(Locale locale)
    {
        localizations.computeIfAbsent(locale, key -> new HashMap<>());
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
