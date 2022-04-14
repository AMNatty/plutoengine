package org.plutoengine.graphics.gui.command;

import org.plutoengine.graphics.gl.vao.attrib.AttributeInfo;
import org.plutoengine.libra.command.impl.LiCommand;
import org.plutoengine.libra.command.impl.LiCommandDrawMesh;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public abstract sealed class PlutoCommandDrawMesh extends LiCommandDrawMesh permits PlutoCommandDrawMeshDirectBuffer, PlutoCommandDrawMeshHeap
{
    protected final Map<Integer, AttributeInfo> attributeInfo;
    protected final Map<Integer, Buffer> data;
    protected IntBuffer indices;

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

    public abstract void addIndices(IntBuffer data);

    public void addAttribute(int attrib, float[] data, int dimensions)
    {
        if (data == null)
            return;

        this.addAttribute(attrib, FloatBuffer.wrap(data), dimensions);
    }

    public abstract void addAttribute(int attrib, FloatBuffer data, int dimensions);

    public void addAttribute(int attrib, int[] data, int dimensions)
    {
        if (data == null)
            return;

        this.addAttribute(attrib, IntBuffer.wrap(data), dimensions);
    }

    public abstract void addAttribute(int attrib, IntBuffer data, int dimensions);

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

        this.addIndices(pcdm.getIndices());

        return this;
    }
}
