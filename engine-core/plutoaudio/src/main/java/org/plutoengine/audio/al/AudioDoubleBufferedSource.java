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

package org.plutoengine.audio.al;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.SOFTDirectChannels;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.audio.IAudio;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract class AudioDoubleBufferedSource extends AudioSource
{
    private static final int BUFFER_SIZE_PER_CHANNEL = 8192;
    private static final int DOUBLE_BUFFER = 2;

    protected final int format;

    protected final int channels;

    protected final int sampleRate;

    private final IntBuffer bufferIDs;
    protected final Map<Integer, AudioBuffer> buffers;

    protected final ShortBuffer pcmTransferBuf;

    protected boolean audioBufferDepleted;

    protected boolean closed;

    protected AudioDoubleBufferedSource(IAudio audio)
    {
        this.format = switch (audio.getChannels()) {
            case 1 -> AL10.AL_FORMAT_MONO16;
            case 2 -> AL10.AL_FORMAT_STEREO16;
            default -> throw new UnsupportedOperationException("Unsupported number of channels: " + audio.getChannels());
        };

        this.channels = audio.getChannels();
        this.sampleRate = audio.getSampleRate();

        int bufferSize = audio.getChannels() * BUFFER_SIZE_PER_CHANNEL;

        this.pcmTransferBuf = MemoryUtil.memAllocShort(bufferSize);

        this.bufferIDs = MemoryUtil.memCallocInt(DOUBLE_BUFFER);
        AL10.alGenBuffers(this.bufferIDs);
        this.buffers = IntStream.range(0, this.bufferIDs.limit())
                                .mapToObj(i -> new AudioBuffer(this.bufferIDs.get(i), this.format, this.sampleRate))
                                .collect(Collectors.toMap(AudioBuffer::getID, Function.identity(), (l, r) -> l));

        AL10.alSourcei(this.id, SOFTDirectChannels.AL_DIRECT_CHANNELS_SOFT, AL10.AL_TRUE);
    }

    public boolean play()
    {
        if (this.closed)
            return false;

        var state = AL10.alGetSourcei(this.id, AL10.AL_SOURCE_STATE);

        return switch (state)
        {
            case AL10.AL_PLAYING -> true;
            case AL10.AL_PAUSED, AL10.AL_STOPPED -> super.play();
            case AL10.AL_INITIAL -> {
                this.buffers.values()
                            .forEach(this::stream);

                yield super.play();
            }
            default -> false; // Unexpected value, just say it didn't play
        };
    }

    private void stream(AudioBuffer buffer)
    {
        if (this.audioBufferDepleted)
            return;

        this.fillTransferBuffer();

        if (this.audioBufferDepleted)
            return;

        buffer.writeData(this.pcmTransferBuf);
        AL10.alSourceQueueBuffers(this.id, buffer.getID());
    }

    private void fillTransferBuffer()
    {
        this.pcmTransferBuf.clear();
        int samplesPerChannel = this.getSamples(this.pcmTransferBuf);

        if (samplesPerChannel < BUFFER_SIZE_PER_CHANNEL)
        {
            this.audioBufferDepleted = true;
            return;
        }

        var samples = samplesPerChannel * this.channels;
        this.pcmTransferBuf.limit(samples);
    }

    protected abstract int getSamples(ShortBuffer pcmTransferBuf);

    protected List<AudioBuffer> unqueueBuffers()
    {
        int processed = AL10.alGetSourcei(this.id, AL10.AL_BUFFERS_PROCESSED);
        var unqueued = new ArrayList<AudioBuffer>(DOUBLE_BUFFER);

        for (int i = 0; i < processed; i++)
        {
            int bufID = AL10.alSourceUnqueueBuffers(this.id);
            var buffer = this.buffers.get(bufID);
            unqueued.add(buffer);
        }

        return unqueued;

    }

    public boolean update()
    {
        if (this.isClosed())
            return false;

        var unqueued = this.unqueueBuffers();
        unqueued.forEach(this::stream);

        if (AL10.alGetSourcei(this.id, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
        {
            if (this.audioBufferDepleted)
                return false;

            if (unqueued.size() == DOUBLE_BUFFER)
                return super.play();

            return false;
        }

        return true;
    }

    public boolean updateOrClose()
    {
        boolean shouldClose = !this.update();

        if (shouldClose)
            this.close();

        return shouldClose;
    }

    public boolean isClosed()
    {
        return this.closed;
    }

    @Override
    public void close()
    {
        if (this.isClosed())
            return;

        this.closed = true;

        this.stop();

        this.unqueueBuffers();

        super.close();

        this.buffers.values()
                    .forEach(AudioBuffer::close);

        MemoryUtil.memFree(this.pcmTransferBuf);
    }
}
