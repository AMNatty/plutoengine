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

package org.plutoengine.graphics;

import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;
import org.plutoengine.graphics.gl.DrawMode;
import org.plutoengine.graphics.vao.QuadPresets;
import org.plutoengine.graphics.vao.VertexArray;
import org.plutoengine.graphics.texture.texture2d.Texture2D;
import org.plutoengine.util.color.IRGBA;

import java.util.Stack;

/**
 * A builder-like renderer for 2D rectangles. Note that the internal state is
 * not monitored for performance reasons and outside events may affect active
 * instances, such as changing the active shader or texture. In order to restore
 * the internal state to the default.
 * 
 * @author 493msi
 */
public class Renderer2D
{
    public static VertexArray standardQuad;
    public static VertexArray centeredQuad;

    private static IShader2D defaultShader;

    public static final Renderer2D INSTANCE = new Renderer2D();

    protected Matrix3x2f transformation = new Matrix3x2f();

    protected IShader2D customShader;

    protected VertexArray activeVA;

    protected Texture2D activeTexture;

    protected boolean modifiedTransformation = false;

    private static final Stack<IShader2D> customShaderStack = new Stack<>();

    public static void load(IShader2D defaultShaderIn)
    {
        standardQuad = QuadPresets.basicNoNeg();
        centeredQuad = QuadPresets.halvedSize();
        defaultShader = defaultShaderIn;
    }

    public static void unload()
    {
        if (standardQuad != null)
        {
            standardQuad.close();
        }

        if (centeredQuad != null)
        {
            centeredQuad.close();
        }
    }

    /**
     * Pushes a custom {@link IShader2D shader} to be used in place of the
     * default one.
     */
    public static void pushCustomShader(IShader2D shader)
    {
        customShaderStack.push(shader);
    }

    /**
     * Removes the top {@link IShader2D shader} from the custom shader stack.
     */
    public static IShader2D popCustomShader()
    {
        return customShaderStack.pop();
    }

    /**
     * Checks if the renderer is currently supplied with a custom
     * {@link IShader2D shader}.
     */
    public static boolean hasCustomShader()
    {
        return !customShaderStack.empty();
    }

    public static Renderer2D draw(VertexArray va, IShader2D shader)
    {

        GL33.glEnable(GL33.GL_BLEND);

        INSTANCE.customShader = shader;

        INSTANCE.customShader.start();

        INSTANCE.customShader.loadRecolor(1, 1, 1, 1);

        INSTANCE.identity();

        INSTANCE.switchVertexArray(va);

        INSTANCE.activeTexture = null;

        return INSTANCE;
    }

    public static Renderer2D draw(VertexArray va)
    {
        return draw(va, hasCustomShader() ? customShaderStack.peek() : defaultShader);
    }

    public static Renderer2D draw()
    {
        return draw(standardQuad);
    }

    public Renderer2D identity()
    {
        this.transformation.identity();

        return this;
    }

    public Renderer2D switchVertexArray(VertexArray va)
    {
        va.bind();
        va.enableAllAttributes();

        this.activeVA = va;

        return this;
    }

    public Renderer2D rotate(float rotation)
    {
        this.transformation.rotate(rotation);
        return this;
    }

    public Renderer2D at(float x, float y, float width, float height)
    {
        this.identity();
        this.transformation.translate(x, y);
        this.transformation.scale(width, height);

        this.modifiedTransformation = true;

        return this;
    }

    public Renderer2D translate(float x, float y)
    {
        this.transformation.translate(x, y);

        this.modifiedTransformation = true;

        return this;
    }

    public Renderer2D scale(float width, float height)
    {
        this.transformation.scale(width, height);

        this.modifiedTransformation = true;

        return this;
    }

    public Renderer2D transformation(Matrix3x2fc transformationMatrix)
    {
        this.transformation.set(transformationMatrix);

        this.modifiedTransformation = true;

        return this;
    }

    private Renderer2D writeTransformation()
    {
        this.customShader.loadTransformationMatrix(this.transformation);

        this.modifiedTransformation = false;

        return this;
    }

    public Renderer2D texturef(Texture2D texture, float u, float v, float width, float height)
    {
        if (this.activeTexture != texture)
        {
            this.activeTexture = texture;
            texture.bind();
        }

        this.customShader.loadUV(u, 1 - v - height, width, height);

        return this;
    }

    public Renderer2D texture(Texture2D texture, int u, int v, int width, int height)
    {
        return this.texturef(texture, u / (float) texture.getWidth(), v / (float) texture.getHeight(), width / (float) texture.getWidth(), height / (float) texture.getHeight());
    }

    public Renderer2D texture(Texture2D texture)
    {
        return this.texturef(texture, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public Renderer2D recolor(float r, float g, float b, float a)
    {
        this.customShader.loadRecolor(r, g, b, a);

        return this;
    }

    public Renderer2D recolor(IRGBA rgba)
    {
        this.customShader.loadRecolor(rgba.red(), rgba.green(), rgba.blue(), rgba.alpha());

        return this;
    }

    public Renderer2D recolor(Vector4f recolor)
    {
        this.customShader.loadRecolor(recolor);

        return this;
    }

    public void flush()
    {
        if (this.modifiedTransformation)
        {
            this.writeTransformation();
            this.modifiedTransformation = false;
        }

        this.activeVA.draw(DrawMode.TRIANGLES);
    }
}
