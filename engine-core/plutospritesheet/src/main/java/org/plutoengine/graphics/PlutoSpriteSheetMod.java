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

import org.plutoengine.Pluto;
import org.plutoengine.graphics.shader.RenderShaderBuilder;
import org.plutoengine.graphics.spritesheet.FramebufferTiledSpriteSheet;
import org.plutoengine.mod.IModEntryPoint;
import org.plutoengine.mod.Mod;
import org.plutoengine.mod.ModEntry;

@ModEntry(modID = PlutoSpriteSheetMod.MOD_ID,
        version = Pluto.VERSION,
        dependencies = { PlutoRendererMod.class })
public class PlutoSpriteSheetMod implements IModEntryPoint
{
    public static final String MOD_ID = "tefek.plutospritesheet";

    public static Mod instance;

    /**
     * Strictly internal use only, do NOT use this outside of plutospritesheet
     */
    private static Shader2D shader2D;

    /**
     * Strictly internal use only, do NOT use this outside of plutospritesheet
     */
    private static ShaderRectangle2D shaderRectangle2D;

    /**
     * Strictly internal use only, do NOT use this outside of plutospritesheet
     */
    private static ShaderRectangle2D spriteSheetShader;

    @Override
    public void onLoad(Mod mod)
    {
        instance = mod;

        shader2D = new RenderShaderBuilder(mod.getResource("shaders.v2D#glsl"), mod.getResource("shaders.f2D#glsl")).build(Shader2D.class, false);
        shaderRectangle2D = new RenderShaderBuilder(mod.getResource("shaders.VertexRectangle2D#glsl"), mod.getResource("shaders.FragmentRectangle2D#glsl")).build(ShaderRectangle2D.class, false);
        spriteSheetShader = new RenderShaderBuilder(mod.getResource("shaders.VertexSpriteSheet#glsl"), mod.getResource("shaders.FragmentSpriteSheet#glsl")).build(ShaderRectangle2D.class, false);

        Renderer2D.load(shader2D);
        RectangleRenderer2D.load(shaderRectangle2D);

        FramebufferTiledSpriteSheet.setSpriteShader(spriteSheetShader);
    }

    @Override
    public void onUnload()
    {
        FramebufferTiledSpriteSheet.setSpriteShader(null);

        spriteSheetShader.close();
        shaderRectangle2D.close();
        shader2D.close();

        RectangleRenderer2D.unload();
        Renderer2D.unload();
    }
}
