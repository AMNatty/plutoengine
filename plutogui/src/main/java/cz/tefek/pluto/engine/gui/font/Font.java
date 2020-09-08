package cz.tefek.pluto.engine.gui.font;

import java.util.HashMap;

import cz.tefek.pluto.engine.graphics.texture.texture2d.RectangleTexture;

public class Font
{
    private String name;
    private int width;
    private int height;
    private HashMap<Character, CharacterInfo> definitions;
    private RectangleTexture texture;

    public Font(String name, int width, int height, HashMap<Character, CharacterInfo> def)
    {
        this.name = name;
        this.width = width;
        this.height = height;
        this.definitions = def;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getTextureWidth()
    {
        return this.width;
    }

    public int getTextureHeight()
    {
        return this.height;
    }

    public RectangleTexture getTexture()
    {
        return this.texture;
    }

    public void setTexture(RectangleTexture texture)
    {
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public HashMap<Character, CharacterInfo> getDefinitions()
    {
        return this.definitions;
    }
}
