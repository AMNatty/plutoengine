package org.plutoengine.audio.util;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

public class AudioUtil
{
    public static void printInfo(long handle, STBVorbisInfo info)
    {
        Logger.log(SmartSeverity.AUDIO, "stream length, samples: " + STBVorbis.stb_vorbis_stream_length_in_samples(handle));
        Logger.log(SmartSeverity.AUDIO, "stream length, seconds: " + STBVorbis.stb_vorbis_stream_length_in_seconds(handle));

        Logger.log(SmartSeverity.AUDIO);

        Logger.log(SmartSeverity.AUDIO, "channels = " + info.channels());
        Logger.log(SmartSeverity.AUDIO, "sampleRate = " + info.sample_rate());
        Logger.log(SmartSeverity.AUDIO, "maxFrameSize = " + info.max_frame_size());
        Logger.log(SmartSeverity.AUDIO, "setupMemoryRequired = " + info.setup_memory_required());
        Logger.log(SmartSeverity.AUDIO, "setupTempMemoryRequired() = " + info.setup_temp_memory_required());
        Logger.log(SmartSeverity.AUDIO, "tempMemoryRequired = " + info.temp_memory_required());
    }
}
