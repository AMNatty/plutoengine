package cz.tefek.pluto.engine.shader;

import org.lwjgl.opengl.GL33;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.io.asl.resource.ResourceSubscriber;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;
import cz.tefek.pluto.engine.shader.type.FragmentShader;
import cz.tefek.pluto.engine.shader.type.VertexShader;
import cz.tefek.pluto.engine.shader.uniform.Uniform;
import cz.tefek.pluto.engine.shader.uniform.UniformBase;
import cz.tefek.pluto.engine.shader.uniform.UniformMat4;
import cz.tefek.pluto.engine.shader.uniform.auto.AutoViewportProjection;
import cz.tefek.pluto.engine.shader.uniform.auto.AutomaticUniforms;

public class RenderShaderBuilder
{
    private final boolean tempShaders;

    private VertexShader vertexShader;
    private FragmentShader fragmentShader;

    public RenderShaderBuilder(ResourceSubscriber subscriber, String vtx, String frg)
    {
        this(new ResourceAddress(subscriber, vtx), new ResourceAddress(subscriber, frg));
    }

    public RenderShaderBuilder(ResourceAddress vtx, ResourceAddress frg)
    {
        this.tempShaders = true;

        this.vertexShader = new VertexShader(vtx);
        this.fragmentShader = new FragmentShader(frg);
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
                    this.vertexShader.dispose();
                    this.fragmentShader.dispose();
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
                    this.vertexShader.dispose();
                    this.fragmentShader.dispose();
                }

                return null;
            }

            program.detach(this.vertexShader);
            program.detach(this.fragmentShader);

            if (this.tempShaders)
            {
                this.vertexShader.dispose();
                this.fragmentShader.dispose();
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
                        if (uniform instanceof UniformMat4)
                        {
                            UniformMat4 umat4 = (UniformMat4) uniform;
                            AutomaticUniforms.VIEWPORT_PROJECTION.addListener(mat4 ->
                            {
                                program.start();
                                umat4.load(mat4);
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
