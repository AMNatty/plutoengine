package cz.tefek.pluto.engine.graphics.texture.sampler;

import java.util.HashSet;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.graphics.texture.MagFilter;
import cz.tefek.pluto.engine.graphics.texture.MinFilter;
import cz.tefek.pluto.engine.graphics.texture.WrapMode;

public class Sampler3D
{
    private int id;

    private HashSet<Integer> usedUnits;

    public Sampler3D()
    {
        this.id = GL33.glGenSamplers();
        this.usedUnits = new HashSet<>(GL33.glGetInteger(GL33.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS));
    }

    public void setFilteringParameters(MinFilter minFilter, MagFilter magFilter, WrapMode wrapModeS, WrapMode wrapModeT, WrapMode wrapModeR)
    {
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_MIN_FILTER, minFilter.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_MAG_FILTER, magFilter.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_WRAP_S, wrapModeS.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_WRAP_T, wrapModeT.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_WRAP_R, wrapModeR.getGLID());
    }

    public void bind(int unit)
    {
        this.usedUnits.add(unit);
        GL33.glBindSampler(unit, this.id);
    }

    public void delete()
    {
        this.unbind();
        GL33.glDeleteSamplers(this.id);
    }

    public void unbind(int unit)
    {
        GL33.glBindSampler(unit, 0);
        this.usedUnits.remove(unit);
    }

    public void unbind()
    {
        this.usedUnits.forEach(this::unbind);
        this.usedUnits.clear();
    }
}
