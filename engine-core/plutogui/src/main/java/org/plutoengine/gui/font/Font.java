package org.plutoengine.gui.font;

import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

import java.util.HashMap;

public class Font
{
    private String name;
    private int width;
    private int height;
    private final HashMap<Character, CharacterInfo> definitions;
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
