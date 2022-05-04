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
