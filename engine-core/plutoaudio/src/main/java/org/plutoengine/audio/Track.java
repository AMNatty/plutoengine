package org.plutoengine.audio;

abstract class Track implements IAudio
{
    protected int channels;
    protected int sampleRate;

    @Override
    public int getChannels()
    {
        return this.channels;
    }

    @Override
    public int getSampleRate()
    {
        return this.sampleRate;
    }
}
