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

import org.jetbrains.annotations.NotNull;

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
    public void write(byte @NotNull [] b) throws IOException
    {
        this.outputStreamA.write(b);
        this.outputStreamB.write(b);
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException
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
