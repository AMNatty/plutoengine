package org.plutoengine.graphics.gui.command;

import org.plutoengine.graphics.gl.vao.attrib.AttributeInfo;
import org.plutoengine.graphics.gl.vbo.EnumArrayBufferType;
import org.plutoengine.libra.command.impl.LiCommand;
import org.plutoengine.libra.command.impl.LiCommandDrawMesh;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class PlutoCommandDrawMesh extends LiCommandDrawMesh
{
    private final Map<Integer, AttributeInfo> attributeInfo;
    private final Map<Integer, Buffer> data;
    private IntBuffer indices;

    public PlutoCommandDrawMesh()
    {
        this.attributeInfo = new TreeMap<>();
        this.data = new TreeMap<>();
    }

    public IntBuffer getIndices()
    {
        if (this.indices == null)
            return null;

        return this.indices;
    }

    public Map<Integer, AttributeInfo> getAttributeInfo()
    {
        return Collections.unmodifiableMap(this.attributeInfo);
    }

    public Map<Integer, Buffer> getData()
    {
        return Collections.unmodifiableMap(this.data);
    }

    public void addIndices(int[] data)
    {
        if (data == null)
            return;

        this.addIndices(IntBuffer.wrap(data));
    }

    public void addIndices(IntBuffer data)
    {
        if (data == null)
            return;

        if (this.indices == null)
            this.indices = IntBuffer.allocate(data.remaining());

        if (this.indices.remaining() < data.remaining())
            this.indices = IntBuffer.allocate(Math.max(this.indices.capacity() << 1, this.indices.capacity() + data.remaining())).put(this.indices.flip());

        this.indices.put(data);
    }

    public void addAttribute(int attrib, float[] data, int dimensions)
    {
        if (data == null)
            return;

        this.addAttribute(attrib, FloatBuffer.wrap(data), dimensions);
    }

    public void addAttribute(int attrib, FloatBuffer data, int dimensions)
    {
        if (data == null)
            return;

        var meta = this.attributeInfo.computeIfAbsent(attrib, k -> new AttributeInfo(EnumArrayBufferType.FLOAT, attrib, dimensions));

        if (meta.dimensions() != dimensions)
            throw new IllegalArgumentException("Attribute dimensions mismatch!");

        this.data.compute(attrib, (k, v) -> {
            if (v == null)
                return FloatBuffer.allocate(data.remaining()).put(data);

            if (!(v instanceof FloatBuffer fab))
                throw new IllegalArgumentException();

            if (data.remaining() <= fab.remaining())
                return fab.put(data);

            var newBuf = FloatBuffer.allocate(Math.max(v.capacity() << 1, v.capacity() + data.remaining()));
            return newBuf.put(fab.flip()).put(data);
        });
    }

    public void addAttribute(int attrib, int[] data, int dimensions)
    {
        if (data == null)
            return;

        this.addAttribute(attrib, IntBuffer.wrap(data), dimensions);
    }

    public void addAttribute(int attrib, IntBuffer data, int dimensions)
    {
        if (data == null)
            return;

        var meta = this.attributeInfo.computeIfAbsent(attrib, k -> new AttributeInfo(EnumArrayBufferType.INT, attrib, dimensions));

        if (meta.dimensions() != dimensions)
            throw new IllegalArgumentException("Attribute dimensions mismatch!");

        this.data.compute(attrib, (k, v) -> {
            if (v == null)
                return IntBuffer.allocate(data.remaining()).put(data);

            if (!(v instanceof IntBuffer fab))
                throw new IllegalArgumentException();

            if (data.remaining() <= fab.remaining())
                return fab.put(data);

            var newBuf = IntBuffer.allocate(Math.max(v.capacity() << 1, v.capacity() + data.remaining()));
            return newBuf.put(fab.flip()).put(data);
        });
    }

    @Override
    public boolean supportsMerge(LiCommand other)
    {
        if (!(other instanceof PlutoCommandDrawMesh pcdm))
            return false;

        return this.attributeInfo.equals(pcdm.attributeInfo);
    }

    @Override
    public PlutoCommandDrawMesh merge(LiCommand other)
    {
        if (!(other instanceof PlutoCommandDrawMesh pcdm))
            throw new UnsupportedOperationException();

        pcdm.data.forEach((k, v) -> {
            var attrInfo = this.attributeInfo.get(k);

            switch (attrInfo.type())
            {
                case FLOAT -> this.addAttribute(k, (FloatBuffer) v, attrInfo.dimensions());
                case INT -> this.addAttribute(k, (IntBuffer) v, attrInfo.dimensions());
                case UNSIGNED_INT -> throw new UnsupportedOperationException();
            }
        });

        this.addIndices(pcdm.indices);

        return this;
    }
}
