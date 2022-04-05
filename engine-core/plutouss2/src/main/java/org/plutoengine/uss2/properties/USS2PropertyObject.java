package org.plutoengine.uss2.properties;

import java.nio.ByteBuffer;

public final class USS2PropertyObject
{
    final USS2PropertySchema schema;

    final int currentVersion;
    final int latestVersion;
    boolean dirty;

    final ByteBuffer data;

    private USS2PropertyObject(ByteBuffer buf, USS2PropertySchema schema)
    {
        this.schema = schema;

        if (buf.limit() != schema.getCapacity())
            throw new IllegalArgumentException("The input ByteBuffer's limit and the USS2PropertySchema size must be the same value!");

        this.currentVersion = 0xff & buf.get(0);
        this.latestVersion = schema.getVersion();

        if (this.currentVersion > this.latestVersion)
            throw new RuntimeException(String.format("The file's version (%d) is newer than what USS2 can read (%d)!", this.currentVersion, this.latestVersion));

        this.data = buf;
    }

    private static USS2PropertyObject upgrade(USS2PropertyObject po)
    {
        var poNew = create(po.schema);
        var props = po.schema.getProperties();
        props.forEach(property -> {
            if (property instanceof USS2PropertySchema.USS2Int ussInt)
            {
                ussInt.write(poNew, ussInt.read(po));
            }
            else if (property instanceof USS2PropertySchema.USS2Long ussLong)
            {
                ussLong.write(poNew, ussLong.read(po));
            }
            else if (property instanceof USS2PropertySchema.USS2Double ussDouble)
            {
                ussDouble.write(poNew, ussDouble.read(po));
            }
            else if (property instanceof USS2PropertySchema.USS2Byte ussByte)
            {
                ussByte.write(poNew, ussByte.read(po));
            }
            else if (property instanceof USS2PropertySchema.USS2String ussString)
            {
                ussString.write(poNew, ussString.read(po));
            }
        });

        return poNew;
    }

    public static USS2PropertyObject create(USS2PropertySchema schema)
    {
        var buf = ByteBuffer.wrap(new byte[schema.getCapacity()]);
        buf.put(0, (byte) schema.getVersion());

        var po = new USS2PropertyObject(buf, schema);
        po.dirty = true;

        return po;
    }

    public static USS2PropertyObject from(ByteBuffer buf, USS2PropertySchema schema)
    {
        var po = new USS2PropertyObject(buf, schema);

        if (po.latestVersion != po.currentVersion)
            return upgrade(po);

        return po;
    }

    public ByteBuffer getData()
    {
        return this.data;
    }

    public byte[] getDataByteArray()
    {
        if (!this.data.hasArray())
            throw new RuntimeException("Data does not have a backing array!");

        return this.data.array();
    }

    public boolean isDirty()
    {
        return this.dirty;
    }
}
