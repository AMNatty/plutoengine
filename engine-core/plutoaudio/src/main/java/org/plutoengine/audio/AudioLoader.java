package org.plutoengine.audio;

import org.lwjgl.system.MemoryUtil;
import org.plutoengine.buffer.BufferHelper;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class AudioLoader
{
    /**
     * Loads an audio track denoted by this {@link Path} into memory
     * for from-memory Vorbis decoding and streaming. Good for frequently used
     * medium-sized audio files, however it is discouraged to use such a track
     * in multiple audio sources at once due to the cost of seeking.
     */
    public static SeekableTrack loadMemoryDecoded(Path path)
    {

        Logger.logf(SmartSeverity.AUDIO_PLUS, "Loading audio file: %s\n", path);

        try
        {
            return new MemoryDecodedVorbisTrack(path);
        }
        catch (IOException e)
        {
            Logger.logf(SmartSeverity.AUDIO_ERROR, "Failed to load an audio file: '%s'\n", path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads an audio track denoted by this {@link Path} into memory
     * for from-memory PCM streaming. Good for frequently used small audio
     * files.
     */
    public static RandomAccessClip loadMemoryPCM(Path path)
    {
        Logger.logf(SmartSeverity.AUDIO_PLUS, "Loading audio file: %s\n", path);

        try
        {
            return new MemoryPCMClip(path);
        }
        catch (IOException e)
        {
            Logger.logf(SmartSeverity.AUDIO_ERROR, "Failed to load an audio file: '%s'\n", path);
            e.printStackTrace();
            return null;
        }
    }

    static ByteBuffer loadIntoMemory(Path path) throws IOException
    {
        var size = Files.size(path);

        if (size > Integer.MAX_VALUE)
            throw new IOException("File '%s' is too big to be loaded!".formatted(path));

        var readData = MemoryUtil.memAlloc((int) size);

        return BufferHelper.readToByteBuffer(path, readData);
    }

}
