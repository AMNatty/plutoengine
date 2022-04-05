package org.plutoengine.graphics.gl.vao;

import org.plutoengine.graphics.gl.vao.attrib.data.VecArray;

/**
 * @author 493msi
 *
 */
public class QuadPresets
{
    public static VertexArray basicQuad()
    {
        float[] uvs = { 0, 0, 1, 0, 1, 1, 0, 1 };

        float[] positions = { -1, 1, 1, 1, 1, -1, -1, -1 };

        int[] indices = { 0, 1, 2, 0, 2, 3 };

        VertexArrayBuilder vab = new VertexArrayBuilder();
        vab.vertices(new VecArray<>(positions, 2));
        vab.uvs(new VecArray<>(uvs, 2));
        vab.indices(new VecArray<>(indices, 1));

        return vab.export();
    }

    public static VertexArray halvedSize()
    {
        float[] uvs = { 0, 0, 1, 0, 1, 1, 0, 1 };

        float[] positions = { -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f };

        int[] indices = { 0, 1, 2, 0, 2, 3 };

        VertexArrayBuilder vab = new VertexArrayBuilder();
        vab.vertices(new VecArray<>(positions, 2));
        vab.uvs(new VecArray<>(uvs, 2));
        vab.indices(new VecArray<>(indices, 1));
        return vab.export();
    }

    public static VertexArray basicNoNeg()
    {
        float[] uvs = { 0, 0, 1, 0, 1, 1, 0, 1 };

        float[] positions = { 0, 1, 1, 1, 1, 0, 0, 0 };

        int[] indices = { 0, 1, 2, 0, 2, 3 };

        VertexArrayBuilder vab = new VertexArrayBuilder();
        vab.vertices(new VecArray<>(positions, 2));
        vab.uvs(new VecArray<>(uvs, 2));
        vab.indices(new VecArray<>(indices, 1));

        return vab.export();
    }
}
