package cz.tefek.pluto.engine.graphics.gl.vao.attrib.data;

import java.lang.reflect.Array;

public class VecArray<T>
{
    private final T data;
    private final int vecDimensions;
    private final int vertexCount;

    public VecArray(T data, int vecDimensions)
    {
        var dataType = data.getClass();

        if (!dataType.isArray())
        {
            throw new IllegalStateException("Input data must be of an array type!");
        }

        this.data = data;
        this.vecDimensions = vecDimensions;
        var dataLength = Array.getLength(data);

        if (dataLength % vecDimensions != 0)
        {
            throw new IllegalArgumentException("Data size must be divisible by the amount of vector dimensions!");
        }

        this.vertexCount = dataLength / vecDimensions;
    }

    public T getData()
    {
        return this.data;
    }

    public int getVecDimensions()
    {
        return this.vecDimensions;
    }

    public int getVertexCount()
    {
        return this.vertexCount;
    }
}
