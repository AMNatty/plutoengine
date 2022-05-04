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

package org.plutoengine.logger;

/**
 * A more visual way to denote what's actually happening.
 * 
 * @author 493msi
 *
 * @since pre-alpha
 */
public enum SmartSeverity implements ISeverity
{
    ADDED("[+] ", false),
    REMOVED("[-] ", false),
    CHECK("[✓] ", false),

    ZERO("[0] ", false),

    INFO("[i] ", false),
    WARNING("[!] ", true),
    ERROR("[X] ", true),

    AUDIO("[i] [♪] ", false),
    AUDIO_PLUS("[+] [♪] ", false),
    AUDIO_MINUS("[-] [♪] ", false),
    AUDIO_WARNING("[!] [♪] ", true),
    AUDIO_ERROR("[X] [♪] ", true),
    AUDIO_CHECK("[✓] [♪] ", true),

    MODULE("[i] [M] ", false),
    MODULE_PLUS("[+] [M] ", false),
    MODULE_MINUS("[-] [M] ", false),
    MODULE_WARNING("[!] [M] ", true),
    MODULE_ERROR("[X] [M] ", true),
    MODULE_CHECK("[✓] [M] ", false),

    EVENT("[i] [E] ", false),
    EVENT_PLUS("[+] [E] ", false),
    EVENT_MINUS("[-] [E] ", false),
    EVENT_WARNING("[!] [E] ", true),
    EVENT_ERROR("[X] [E] ", true),
    EVENT_CHECK("[✓] [E] ", true);

    private final String displayName;
    private final boolean usesStdErr;

    SmartSeverity(String name, boolean usesStdErr)
    {
        this.displayName = name;
        this.usesStdErr = usesStdErr;
    }

    @Override
    public String getDisplayName()
    {
        return this.displayName;
    }

    @Override
    public boolean isStdErr()
    {
        return this.usesStdErr;
    }

}
