package cz.tefek.pluto.engine.gui.font;

import org.joml.Vector2fc;

import cz.tefek.pluto.engine.gui.IGUIPipeline;
import cz.tefek.pluto.engine.gui.IGUIRenderer;

public class FontRenderer2 implements IGUIRenderer
{
    private static final FontRenderer2 INSTANCE_IMMEDIATE = new FontRenderer2();
    private static final FontRenderer2 INSTANCE_DEFERRED = new FontRenderer2();

    private final IGUIPipeline deferPipeline;

    private static final float DEFAULT_SIZE = 24;

    private float x;
    private float y;
    private String drawnText;
    private float size = DEFAULT_SIZE;

    private FontRenderer2(IGUIPipeline deferPipeline)
    {
        this.deferPipeline = deferPipeline;
    }

    private FontRenderer2()
    {
        this.deferPipeline = null;
    }

    public FontRenderer2 at(float x, float y)
    {
        this.x = x;
        this.y = y;

        return this;
    }

    public FontRenderer2 at(Vector2fc pos)
    {
        this.x = pos.x();
        this.y = pos.y();

        return this;
    }

    public FontRenderer2 size(float size)
    {
        this.size = size;

        return this;
    }

    public FontRenderer2 string(Object text)
    {
        this.drawnText = String.valueOf(text);

        return this;
    }

    public FontRenderer2 fstring(String format, Object... items)
    {
        this.drawnText = String.format(format, items);

        return this;
    }

    public FontRenderer2 reset()
    {
        this.size = DEFAULT_SIZE;
        this.drawnText = "<null>";
        this.x = 0;
        this.y = 0;

        return this;
    }

    @Override
    public void flush()
    {
        if (this.deferPipeline != null)
        {
            // Defer rendering to the pipeline
        }
        else
        {
            // Draw in immediate mode
        }
    }

    public static FontRenderer2 immediate()
    {
        return INSTANCE_IMMEDIATE.reset();
    }

    public static FontRenderer2 deferred()
    {
        return INSTANCE_DEFERRED.reset();
    }

    public static FontRenderer2 draw()
    {
        return deferred();
    }
}
