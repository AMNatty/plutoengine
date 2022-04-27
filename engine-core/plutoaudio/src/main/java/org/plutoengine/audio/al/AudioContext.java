package org.plutoengine.audio.al;

import org.joml.Matrix4x3f;
import org.joml.Matrix4x3fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.Pluto;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author 493msi
 *
 */
public class AudioContext extends PlutoLocalComponent
{
    private long device = MemoryUtil.NULL;
    private long context = MemoryUtil.NULL;
    private ALCapabilities capabilities;

    private Matrix4x3fc transformation;

    AudioContext()
    {
        this.transformation = new Matrix4x3f();
    }

    @Override
    protected void onMount(ComponentDependencyManager manager)
    {
        var devicePtr = ALC10.alcOpenDevice((ByteBuffer) null);
        if (devicePtr == MemoryUtil.NULL)
        {
            Logger.log(SmartSeverity.AUDIO_ERROR, "Failed to open the default audio device.");

            // No audio device found, but the game should not crash just because we have no audio
            return;
        }

        this.device = devicePtr;

        var contextPtr = ALC10.alcCreateContext(devicePtr, (IntBuffer) null);
        if (contextPtr == MemoryUtil.NULL)
        {
            ALC10.alcCloseDevice(devicePtr);

            Logger.log(SmartSeverity.AUDIO_ERROR, "Failed to create an OpenAL context.");

            // The game should not crash just because we have no audio
            return;
        }

        this.context = contextPtr;

        EXTThreadLocalContext.alcSetThreadContext(contextPtr);

        ALCCapabilities deviceCaps = ALC.createCapabilities(devicePtr);
        var alCapabilities = AL.createCapabilities(deviceCaps);

        if (Pluto.DEBUG_MODE)
        {
            Logger.logf(SmartSeverity.AUDIO, "OpenAL10: %b\n", alCapabilities.OpenAL10);
            Logger.logf(SmartSeverity.AUDIO, "OpenAL11: %b\n", alCapabilities.OpenAL11);
            Logger.logf(SmartSeverity.AUDIO, "Distance model: %s\n", EnumDistanceModel.getByID(AL11.alGetInteger(AL11.AL_DISTANCE_MODEL)));
        }

        this.capabilities = alCapabilities;

        Logger.log(SmartSeverity.AUDIO_PLUS, "Audio engine started.");
    }

    public void setTransformation(Matrix4x3fc transformation)
    {
        this.transformation = transformation;
    }

    public Vector3fc transform(Vector3fc vector)
    {
        return this.transformation.transformPosition(vector, new Vector3f());
    }

    public void setDistanceModel(EnumDistanceModel model)
    {
        AL10.alDistanceModel(model.getALID());
    }

    public void setSpeedOfSound(float speedOfSound)
    {
        AL11.alSpeedOfSound(speedOfSound);
    }

    public void setSpeed(Vector3fc speed)
    {
        var tSpeed = this.transformation.transformPosition(speed, new Vector3f());
        AL10.alListener3f(AL10.AL_VELOCITY, tSpeed.x(), tSpeed.y(), tSpeed.z());
    }

    public void setPosition(Vector3f position)
    {
        var tPosition = this.transformation.transformPosition(position, new Vector3f());
        AL10.alListener3f(AL10.AL_POSITION, tPosition.x(), tPosition.y(), tPosition.z());
    }

    public void setVolume(float volume)
    {
        AL10.alListenerf(AL10.AL_GAIN, volume);
    }

    public void setOrientation(Vector3f at, Vector3f up)
    {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        AL10.alListenerfv(AL10.AL_ORIENTATION, data);
    }

    public boolean isReady()
    {
        return this.capabilities != null;
    }

    @Override
    protected void onUnmount()
    {
        Logger.log(SmartSeverity.AUDIO_MINUS, "Shutting down the audio engine.");

        EXTThreadLocalContext.alcSetThreadContext(MemoryUtil.NULL);

        ALC10.alcDestroyContext(this.context);
        ALC10.alcCloseDevice(this.device);

        this.context = MemoryUtil.NULL;
        this.device = MemoryUtil.NULL;
        this.capabilities = null;
    }

    @Override
    public boolean isUnique()
    {
        return true;
    }
}
