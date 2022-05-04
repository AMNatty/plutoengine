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

package org.plutoengine.uss2.properties;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class USS2PropertySchema
{
    public static final int DOES_NOT_EXIST_IN_VERSION = -1;
    public static final int MAX_VERSION = 0xff;

    private final List<USS2Property> properties;

    private final int capacity;

    private int version;
    private final int[] offsets;

    protected USS2PropertySchema(int capacity)
    {
        if (capacity < 16)
            throw new IllegalArgumentException("Please use a starting capacity of at least 16 bytes.");

        this.properties = new ArrayList<>();
        this.offsets = new int[MAX_VERSION + 1];

        // Version byte
        Arrays.fill(this.offsets, Byte.BYTES);

        this.capacity = capacity;
        this.version = 0;
    }

    public final String getInfo()
    {
        float used = this.offsets[MAX_VERSION] / (float) this.capacity;
        final int bars = 30;
        int usedBars = Math.round(used * bars);

        return String.format("[%s%s] %.2f%% of schema used (%d/%d bytes).", "|".repeat(usedBars), " ".repeat(bars - usedBars), used * 100, this.offsets[MAX_VERSION], this.capacity);
    }

    private void declareProperty(USS2Property property)
    {
        Arrays.fill(property.offsets, 0, property.version, DOES_NOT_EXIST_IN_VERSION);
        System.arraycopy(this.offsets, property.version, property.offsets, property.version, property.offsets.length - property.version);

        // No need to check all versions, because the highest version is the largest by default (properties cannot be removed)
        if (this.offsets[MAX_VERSION] + property.objectSize > this.capacity)
            throw new IllegalStateException(
                String.format("Error: Declaring another property (of size %d) " +
                              "would put the schema's total size to %d, which is over the capacity of %d.",
                              property.objectSize,
                              this.offsets[MAX_VERSION] + property.objectSize,
                              this.capacity));

        this.properties.add(property);

        if (property.version > this.version)
            this.version = property.version;

        for (int i = property.version; i <= MAX_VERSION; i++)
            this.offsets[i] += property.objectSize;
    }

    public final List<USS2Property> getProperties()
    {
        return Collections.unmodifiableList(this.properties);
    }

    public final int getVersion()
    {
        return this.version;
    }

    public final int getCapacity()
    {
        return this.capacity;
    }

    protected final USS2Int declareInt(byte version)
    {
        var ussInt = new USS2Int(0xff & version);
        this.declareProperty(ussInt);
        return ussInt;
    }

    protected final USS2Long declareLong(byte version)
    {
        var ussLong = new USS2Long(0xff & version);
        this.declareProperty(ussLong);
        return ussLong;
    }

    protected final USS2Double declareDouble(byte version)
    {
        var ussDouble = new USS2Double(0xff & version);
        this.declareProperty(ussDouble);
        return ussDouble;
    }

    protected final USS2Byte declareByte(byte version)
    {
        var ussByte = new USS2Byte(0xff & version);
        this.declareProperty(ussByte);
        return ussByte;
    }

    protected final USS2String declareString(byte version, byte length)
    {
        var ussByte = new USS2String(0xff & version, 0xff & length);
        this.declareProperty(ussByte);
        return ussByte;
    }

    public static abstract class USS2Property
    {
        private final int version;
        private final int objectSize;
        protected final int[] offsets;

        protected USS2Property(int version, int objectSize)
        {
            this.version = version;
            this.objectSize = objectSize;
            this.offsets = new int[MAX_VERSION];
        }
    }

    public static final class USS2Int extends USS2Property
    {
        private USS2Int(int version)
        {
            super(version, Integer.BYTES);
        }

        public void write(USS2PropertyObject po, int value)
        {
            po.dirty = true;
            po.data.putInt(this.offsets[po.currentVersion], value);
        }

        public int read(USS2PropertyObject po)
        {
            if (this.offsets[po.currentVersion] == DOES_NOT_EXIST_IN_VERSION)
            {
                return 0;
            }

            return po.data.getInt(this.offsets[po.currentVersion]);
        }
    }

    public static final class USS2Long extends USS2Property
    {
        private USS2Long(int version)
        {
            super(version, Long.BYTES);
        }

        public void write(USS2PropertyObject po, long value)
        {
            po.dirty = true;
            po.data.putLong(this.offsets[po.currentVersion], value);
        }

        public long read(USS2PropertyObject po)
        {
            if (this.offsets[po.currentVersion] == DOES_NOT_EXIST_IN_VERSION)
            {
                return 0;
            }

            return po.data.getLong(this.offsets[po.currentVersion]);
        }
    }

    public static final class USS2Double extends USS2Property
    {
        private USS2Double(int version)
        {
            super(version, Double.BYTES);
        }

        public void write(USS2PropertyObject po, double value)
        {
            po.dirty = true;
            po.data.putDouble(this.offsets[po.currentVersion], value);
        }

        public double read(USS2PropertyObject po)
        {
            if (this.offsets[po.currentVersion] == DOES_NOT_EXIST_IN_VERSION)
            {
                return 0.0;
            }

            return po.data.getDouble(this.offsets[po.currentVersion]);
        }
    }

    public static final class USS2Byte extends USS2Property
    {
        public USS2Byte(int version)
        {
            super(version, Byte.BYTES);
        }

        public void write(USS2PropertyObject po, byte value)
        {
            po.dirty = true;
            po.data.put(this.offsets[po.currentVersion], value);
        }

        public byte read(USS2PropertyObject po)
        {
            if (this.offsets[po.currentVersion] == DOES_NOT_EXIST_IN_VERSION)
            {
                return 0;
            }

            return po.data.get(this.offsets[po.currentVersion]);
        }
    }

    public static final class USS2String extends USS2Property
    {
        private USS2String(int version, int length)
        {
            super(version, Byte.BYTES + length);
        }

        public void write(USS2PropertyObject po, String value)
        {
            po.dirty = true;
            var offset = this.offsets[po.currentVersion];
            var data = value.getBytes(StandardCharsets.UTF_8);
            po.data.position(offset);
            po.data.put((byte) data.length);
            po.data.put(data);
            po.data.rewind();
        }

        public String read(USS2PropertyObject po)
        {
            if (this.offsets[po.currentVersion] == DOES_NOT_EXIST_IN_VERSION)
            {
                return "";
            }

            var offset = this.offsets[po.currentVersion];
            po.data.position(offset);
            var length = po.data.get();
            var bytes = new byte[0xff & length];
            po.data.get(bytes);
            po.data.rewind();

            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
