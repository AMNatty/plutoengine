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

package org.plutoengine.graphics.shader;

import org.lwjgl.opengl.GL33;
import org.plutoengine.graphics.shader.type.FragmentShader;
import org.plutoengine.graphics.shader.type.VertexShader;
import org.plutoengine.graphics.shader.uniform.Uniform;
import org.plutoengine.graphics.shader.uniform.UniformBase;
import org.plutoengine.graphics.shader.uniform.UniformMat4;
import org.plutoengine.graphics.shader.uniform.auto.AutoViewportProjection;
import org.plutoengine.graphics.shader.uniform.auto.AutomaticUniforms;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;

public class RenderShaderBuilder
{
    private final boolean tempShaders;

    private VertexShader vertexShader;
    private FragmentShader fragmentShader;

    public RenderShaderBuilder(Path vtx, Path frg)
    {
        this.tempShaders = true;

        try
        {
            this.vertexShader = new VertexShader(vtx);
            this.fragmentShader = new FragmentShader(frg);
        }
        catch (UncheckedIOException e)
        {
            Logger.log(e);
        }
    }

    public RenderShaderBuilder(VertexShader vtx, FragmentShader frg)
    {
        this.tempShaders = false;

        this.vertexShader = vtx;
        this.fragmentShader = frg;
    }

    // TODO Geometry shader

    public <T extends ShaderBase> T build(Class<T> shaderClass, boolean manualAttributeLayout)
    {
        if (this.vertexShader == null || this.fragmentShader == null)
            return null;

        try
        {
            var programAnnotation = shaderClass.getDeclaredAnnotation(ShaderProgram.class);

            if (programAnnotation == null)
            {
                Logger.logf(SmartSeverity.ERROR, "Shader program class '%s' is not properly annotated with '@%s', it will NOT be loaded.\n", shaderClass.getCanonicalName(), ShaderProgram.class.getName());

                return null;
            }

            if ((shaderClass.getModifiers() & Modifier.FINAL) == 0)
            {
                Logger.logf(SmartSeverity.WARNING, "Shader program class '%s' is not final, this is not enforced, but generally shader program classes should be final.\n", shaderClass.getCanonicalName());
            }

            var programID = GL33.glCreateProgram();

            var shaderConstructor = shaderClass.getConstructor();

            var fields = shaderClass.getFields();

            var program = shaderConstructor.newInstance();

            var programIDField = ShaderBase.class.getDeclaredField("programID");
            programIDField.setAccessible(true);
            programIDField.setInt(program, programID);
            programIDField.setAccessible(false);

            program.attach(this.vertexShader);
            program.attach(this.fragmentShader);

            for (var f : fields)
            {
                var vaa = f.getAnnotation(VertexArrayAttribute.class);

                var vertexAttributeName = f.getName();

                if (vaa == null)
                {
                    continue;
                }

                var vaaName = vaa.name();

                if (!vaaName.isBlank())
                {
                    vertexAttributeName = vaaName;
                }

                if (!manualAttributeLayout)
                {
                    int attribID = vaa.value();
                    program.bindAttribute(attribID, vertexAttributeName);
                    f.setInt(program, attribID);
                }
                else
                {
                    f.setInt(program, GL33.glGetAttribLocation(programID, vertexAttributeName));
                }
            }

            GL33.glLinkProgram(programID);

            if (GL33.glGetProgrami(programID, GL33.GL_LINK_STATUS) != GL33.GL_TRUE)
            {
                Logger.log(SmartSeverity.ERROR, "Shader program could not be linked: " + programID);
                Logger.log(GL33.glGetProgramInfoLog(programID));

                program.detach(this.vertexShader);
                program.detach(this.fragmentShader);

                if (this.tempShaders)
                {
                    this.vertexShader.close();
                    this.fragmentShader.close();
                }

                return null;
            }

            GL33.glValidateProgram(programID);

            if (GL33.glGetProgrami(programID, GL33.GL_VALIDATE_STATUS) != GL33.GL_TRUE)
            {
                Logger.log(SmartSeverity.ERROR, "Shader program could not be validated: " + programID);
                Logger.log(GL33.glGetProgramInfoLog(programID));

                program.detach(this.vertexShader);
                program.detach(this.fragmentShader);

                if (this.tempShaders)
                {
                    this.vertexShader.close();
                    this.fragmentShader.close();
                }

                return null;
            }

            program.detach(this.vertexShader);
            program.detach(this.fragmentShader);

            if (this.tempShaders)
            {
                this.vertexShader.close();
                this.fragmentShader.close();
            }

            for (var field : fields)
            {
                var uniformTag = field.getAnnotation(Uniform.class);

                var uniformName = field.getName();

                if (uniformTag == null)
                {
                    continue;
                }

                var uniformTagName = uniformTag.name();

                if (!uniformTagName.isBlank())
                {
                    uniformName = uniformTagName;
                }

                var type = field.getType();

                if (!UniformBase.class.isAssignableFrom(type))
                {
                    Logger.logf(SmartSeverity.ERROR, "Shader uniform '%s' is not of the %s type in a program ID '%d' of type '%s'.\n", uniformName, UniformBase.class.getCanonicalName(), programID, shaderClass.getCanonicalName());
                    continue;
                }

                var uniformType = type.asSubclass(UniformBase.class);

                int location = GL33.glGetUniformLocation(programID, uniformName);

                if (location == GL33.GL_INVALID_INDEX)
                {
                    field.set(program, null);
                    Logger.logf(SmartSeverity.WARNING, "Did not find shader uniform '%s' in a program ID '%d' of type '%s'.\n", uniformName, programID, shaderClass.getCanonicalName());

                    continue;
                }

                try
                {
                    var uniformConstructor = uniformType.getConstructor(int.class);

                    var uniform = uniformConstructor.newInstance(location);

                    field.set(program, uniform);

                    if (field.getAnnotation(AutoViewportProjection.class) != null)
                    {
                        if (uniform instanceof UniformMat4 umat4)
                        {
                            AutomaticUniforms.VIEWPORT_PROJECTION.addListener(mat4 ->
                            {
                                if (program.getID() == 0)
                                    return false;

                                program.start();
                                umat4.load(mat4);

                                return true;
                            });
                            Logger.logf(SmartSeverity.ADDED, "Uniform '%s' ID %d in '%s' ID %d now listens to AutomaticUniforms.VIEWPORT_PROJECTION.\n", uniformName, location, shaderClass.getCanonicalName(), programID);
                        }
                        else
                        {
                            Logger.logf(SmartSeverity.WARNING, "Uniform '%s' in '%s' is not of type '%s', cannot register for AutomaticUniforms.VIEWPORT_PROJECTION.\n", uniformName, UniformMat4.class.getSimpleName(), shaderClass.getCanonicalName());
                        }
                    }
                }
                catch (NoSuchMethodException e)
                {
                    Logger.logf(SmartSeverity.ERROR, "Shader uniform class '%s' must feature a constructor taking a parameter of type 'int'.\n", uniformType.getCanonicalName());
                    e.printStackTrace();
                }

            }

            return program;
        }
        catch (IllegalArgumentException | NoSuchMethodException e)
        {
            Logger.logf(SmartSeverity.ERROR, "Shader program class '%s' does not have a valid default constructor.\n", shaderClass.getCanonicalName());
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            Logger.log(SmartSeverity.ERROR, "Caught an exception while reflectively invoking a method:");
            e.printStackTrace();
        }
        catch (SecurityException | ReflectiveOperationException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
