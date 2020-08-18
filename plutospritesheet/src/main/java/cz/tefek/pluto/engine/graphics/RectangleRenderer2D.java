package cz.tefek.pluto.engine.graphics;

import java.util.Stack;

import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.graphics.gl.DrawMode;
import cz.tefek.pluto.engine.graphics.gl.vao.QuadPresets;
import cz.tefek.pluto.engine.graphics.gl.vao.VertexArray;
import cz.tefek.pluto.engine.graphics.sprite.Sprite;
import cz.tefek.pluto.engine.graphics.texture.texture2d.RectangleTexture;

/**
 * A builder-like renderer for 2D rectangles. Note that the internal state is
 * not monitored for performance reasons and outside events may affect active
 * instances, such as changing the active shader or texture. In order to restore
 * the internal state to the default.
 * 
 * <p>
 * In contrast to {@link Renderer2D}, {@link RectangleRenderer2D} uses
 * {@link RectangleTexture}s instead of standard 2D textures.
 * </p>
 * 
 * @author 493msi
 */
public class RectangleRenderer2D
{
    static VertexArray standardQuad;
    static VertexArray centeredQuad;

    private static ShaderRectangle2D defaultShader;

    public static final RectangleRenderer2D INSTANCE = new RectangleRenderer2D();

    protected Matrix3x2f transformation = new Matrix3x2f();

    protected IRectangleShader2D customShader;

    protected VertexArray activeVA;

    protected RectangleTexture activeTexture;

    protected boolean modifiedTransformation = false;

    private static Stack<IRectangleShader2D> customShaderStack = new Stack<>();

    public static void load(ShaderRectangle2D defaultShaderIn)
    {
        standardQuad = QuadPresets.basicNoNeg();
        centeredQuad = QuadPresets.halvedSize();

        defaultShader = defaultShaderIn;
    }

    public static void unload()
    {
        if (standardQuad != null)
        {
            standardQuad.delete();
        }

        if (centeredQuad != null)
        {
            centeredQuad.delete();
        }
    }

    /**
     * Pushes a custom {@link IRectangleShader2D shader} to be used in place of
     * the default one.
     */
    public static void pushCustomShader(IRectangleShader2D shader)
    {
        customShaderStack.push(shader);
    }

    /**
     * Removes the top {@link IRectangleShader2D shader} from the custom shader
     * stack.
     */
    public static IRectangleShader2D popCustomShader()
    {
        return customShaderStack.pop();
    }

    /**
     * Checks if the renderer is currently supplied with a custom
     * {@link IRectangleShader2D shader}.
     */
    public static boolean hasCustomShader()
    {
        return !customShaderStack.empty();
    }

    /**
     * Starts drawing, overriding the default shader with the supplied one.
     */
    public static RectangleRenderer2D draw(IRectangleShader2D shader)
    {
        return draw(standardQuad, shader);
    }

    /**
     * Starts drawing, overriding the default shader with the supplied one.
     */
    public static RectangleRenderer2D draw(VertexArray va, IRectangleShader2D shader)
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

    public static RectangleRenderer2D draw(VertexArray va)
    {
        return draw(va, hasCustomShader() ? customShaderStack.peek() : defaultShader);
    }

    public static RectangleRenderer2D draw()
    {
        return draw(standardQuad);
    }

    public RectangleRenderer2D identity()
    {
        this.transformation.m00 = 1;
        this.transformation.m01 = 0;
        this.transformation.m10 = 0;
        this.transformation.m11 = 1;
        this.transformation.m20 = 0;
        this.transformation.m21 = 0;

        return this;
    }

    public RectangleRenderer2D switchVertexArray(VertexArray va)
    {
        va.bind();
        va.enableAllAttributes();

        this.activeVA = va;

        return this;
    }

    public RectangleRenderer2D rotate(float rotation)
    {
        this.transformation.rotate(rotation);
        return this;
    }

    public RectangleRenderer2D at(float x, float y, float width, float height)
    {
        this.identity();
        this.transformation.translate(x, y);
        this.transformation.scale(width, height);

        this.modifiedTransformation = true;

        return this;
    }

    public RectangleRenderer2D translate(float x, float y)
    {
        this.transformation.translate(x, y);

        this.modifiedTransformation = true;

        return this;
    }

    public RectangleRenderer2D scale(float width, float height)
    {
        this.transformation.scale(width, height);

        this.modifiedTransformation = true;

        return this;
    }

    public RectangleRenderer2D transformation(Matrix3x2fc transformationMatrix)
    {
        this.transformation.set(transformationMatrix);

        this.modifiedTransformation = true;

        return this;
    }

    private RectangleRenderer2D writeTransformation()
    {
        this.customShader.loadTransformationMatrix(this.transformation);

        this.modifiedTransformation = false;

        return this;
    }

    public RectangleRenderer2D sprite(Sprite<RectangleTexture> sprite)
    {
        return this.texture(sprite.getSheet(), sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public RectangleRenderer2D texture(RectangleTexture texture, int u, int v, int width, int height)
    {
        if (this.activeTexture != texture)
        {
            this.activeTexture = texture;
            texture.bind();
        }

        this.customShader.loadUV(u, texture.getHeight() - v - height, width, height);

        return this;
    }

    public RectangleRenderer2D texture(RectangleTexture texture)
    {
        return this.texture(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    public RectangleRenderer2D recolor(float r, float g, float b, float a)
    {
        this.customShader.loadRecolor(r, g, b, a);

        return this;
    }

    public RectangleRenderer2D recolor(Vector4f recolor)
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
