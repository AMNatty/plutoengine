package org.plutoengine.graphics.gui;

import org.plutoengine.graphics.gl.DrawMode;
import org.plutoengine.graphics.vao.VertexArray;
import org.plutoengine.graphics.vao.VertexArrayBuilder;
import org.plutoengine.graphics.gui.command.PlutoCommandDrawMesh;
import org.plutoengine.graphics.gui.command.PlutoCommandDrawMeshDirectBuffer;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchShader;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchTexture;
import org.plutoengine.libra.command.AbstractGUICommandParser;
import org.plutoengine.libra.command.IGUIRenderer;
import org.plutoengine.libra.command.LiCommandBuffer;
import org.plutoengine.libra.command.impl.LiCommandSetPaint;
import org.plutoengine.libra.command.impl.LiCommandSetTransform;
import org.plutoengine.libra.command.impl.LiCommandSpecial;
import org.plutoengine.graphics.shader.ShaderBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class PlutoGUICommandParser extends AbstractGUICommandParser
{
    @Override
    protected IGUIRenderer parse(LiCommandBuffer mergedBuffer)
    {
        var drawCalls = new ArrayList<Runnable>();
        var closeCalls = new ArrayDeque<Runnable>();
        var alreadyEnabledAttribs = new HashSet<Integer>();
        var currentShader = (ShaderBase & IGUIShader) null;

        for (var cmd : mergedBuffer)
        {
            var type = cmd.getType();

            switch (type)
            {
                case DRAW_MESH -> {
                    if (!(cmd instanceof PlutoCommandDrawMesh drawCmd))
                        throw new IllegalStateException();

                    var vab = new VertexArrayBuilder();

                    var data = drawCmd.getData();
                    var attrInfo = drawCmd.getAttributeInfo();
                    data.forEach((attr, val) -> vab.attrib(attr, attrInfo.get(attr).dimensions(), val.flip()));

                    var indices = drawCmd.getIndices();
                    if (indices != null)
                        vab.indices(indices.flip());

                    var vao = vab.build();
                    var attribs = vao.getUsedAttributes();
                    var attribsToEnable = new HashSet<>(attribs);
                    attribsToEnable.removeAll(alreadyEnabledAttribs);
                    alreadyEnabledAttribs.addAll(attribsToEnable);

                    if (drawCmd instanceof PlutoCommandDrawMeshDirectBuffer dBuf)
                        dBuf.close();

                    drawCalls.add(() -> {
                        vao.bind();
                        attribsToEnable.forEach(VertexArray::enableAttribute);
                        vao.draw(DrawMode.TRIANGLES);
                    });

                    closeCalls.add(vao::close);
                }

                case SET_TRANSFORM -> {
                    if (!(cmd instanceof LiCommandSetTransform transformCmd))
                        throw new IllegalStateException();

                    var shaderCapture = currentShader;

                    assert shaderCapture != null;

                    drawCalls.add(() -> shaderCapture.setTransform(transformCmd.getTransform()));
                }

                case SET_PAINT -> {
                    if (!(cmd instanceof LiCommandSetPaint paintCmd))
                        throw new IllegalStateException();

                    var shaderCapture = currentShader;

                    assert shaderCapture != null;

                    drawCalls.add(() -> shaderCapture.setPaint(paintCmd.getPaint()));
                }

                case SWITCH_SHADER -> {
                    if (!(cmd instanceof PlutoCommandSwitchShader swSh))
                        throw new IllegalStateException();

                    var shaderCapture = currentShader = (ShaderBase & IGUIShader) swSh.getShader();

                    assert shaderCapture != null;

                    drawCalls.add(() -> shaderCapture.start());
                }

                case SWITCH_TEXTURE -> {
                    if (!(cmd instanceof PlutoCommandSwitchTexture swTx))
                        throw new IllegalStateException();

                    var textureCapture = swTx.getTexture();

                    assert textureCapture != null;

                    drawCalls.add(textureCapture::bind);
                }

                case SPECIAL -> {
                    if (!(cmd instanceof LiCommandSpecial cSp))
                        throw new IllegalStateException();

                    var af = cSp.getAction();
                    drawCalls.add(() -> af.accept(mergedBuffer));
                }
            }
        }

        return new IGUIRenderer()
        {
            @Override
            public void render()
            {
                drawCalls.forEach(Runnable::run);
            }

            @Override
            public void close()
            {
                var it = closeCalls.descendingIterator();
                while (it.hasNext())
                {
                    var call = it.next();
                    call.run();
                }
            }
        };
    }
}
