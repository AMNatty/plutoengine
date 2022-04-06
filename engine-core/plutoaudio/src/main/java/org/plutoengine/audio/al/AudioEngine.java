package org.plutoengine.audio.al;

import org.joml.Vector3f;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.Pluto;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import javax.annotation.concurrent.ThreadSafe;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author 493msi
 *
 */
@ThreadSafe
public class AudioEngine
{
    private static final ThreadLocal<Long> device = ThreadLocal.withInitial(() -> MemoryUtil.NULL);

    private static final ThreadLocal<Long> context = ThreadLocal.withInitial(() -> MemoryUtil.NULL);

    private static final ThreadLocal<ALCapabilities> capabilities = new ThreadLocal<>();

    public static void initialize()
    {
        var devicePtr = ALC10.alcOpenDevice((ByteBuffer) null);
        if (devicePtr == MemoryUtil.NULL)
        {
            Logger.log(SmartSeverity.AUDIO_ERROR, "Failed to open the default audio device.");

            // No audio device found, but the game should not crash just because we have no audio
            return;
        }

        device.set(devicePtr);

        var contextPtr = ALC10.alcCreateContext(devicePtr, (IntBuffer) null);
        if (contextPtr == MemoryUtil.NULL)
        {
            ALC10.alcCloseDevice(devicePtr);

            Logger.log(SmartSeverity.AUDIO_ERROR, "Failed to create an OpenAL context.");

            // The game should not crash just because we have no audio
            return;
        }

        context.set(contextPtr);

        EXTThreadLocalContext.alcSetThreadContext(contextPtr);

        ALCCapabilities deviceCaps = ALC.createCapabilities(devicePtr);
        var alCapabilities = AL.createCapabilities(deviceCaps);

        if (Pluto.DEBUG_MODE)
        {
            Logger.logf(SmartSeverity.AUDIO, "OpenAL10: %b\n", alCapabilities.OpenAL10);
            Logger.logf(SmartSeverity.AUDIO, "OpenAL11: %b\n", alCapabilities.OpenAL11);
        }

        capabilities.set(alCapabilities);
    }

    public static void setSpeed(Vector3f speed)
    {
        AL10.alListener3f(AL10.AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    public static void setPosition(Vector3f position)
    {
        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public static void setVolume(float volume)
    {
        AL10.alListenerf(AL10.AL_GAIN, volume);
    }

    public static void setOrientation(Vector3f at, Vector3f up)
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

    public static boolean isReady()
    {
        return capabilities.get() != null;
    }

    public static void exit()
    {
        EXTThreadLocalContext.alcSetThreadContext(MemoryUtil.NULL);
        ALC10.alcDestroyContext(context.get());
        ALC10.alcCloseDevice(device.get());

        context.remove();
        device.remove();
    }
}
