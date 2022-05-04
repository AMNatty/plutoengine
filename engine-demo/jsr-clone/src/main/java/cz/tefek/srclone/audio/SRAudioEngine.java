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

package cz.tefek.srclone.audio;

import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import org.plutoengine.PlutoLocal;
import org.plutoengine.audio.RandomAccessClip;
import org.plutoengine.audio.al.AudioContext;
import org.plutoengine.audio.al.AudioEngine;
import org.plutoengine.audio.al.AudioTrackSource;
import org.plutoengine.audio.al.SoundEffect;

import cz.tefek.srclone.Game;
import cz.tefek.srclone.SRCloneMod;

public class SRAudioEngine
{
    private AudioTrackSource music;

    private final AudioEngine audioEngine;
    private final AudioContext context;

    public SRAudioEngine()
    {
        this.audioEngine = PlutoLocal.components().getComponent(AudioEngine.class);
        this.context = this.audioEngine.getContext();
        this.context.setTransformation(new Matrix4x3f().scale(1 / 1000.0f, 1 / 1000.0f, 1.0f));
    }

    public void tick(Game game)
    {
        var player = game.getEntityPlayer();

        this.context.setPosition(new Vector3f(player.getX(), player.getY(), -1));

        if (this.music == null && !game.isOver())
        {
            this.music = new AudioTrackSource(SRCloneMod.playingMusic);
            this.music.volume(0.1f);
            this.music.play();
        }
        else if (this.music != null && !this.music.update())
            this.music.play();
    }

    public void stopMusic(Game game)
    {
        if (this.music != null)
        {
            var mute = 1 - game.getDeathScreenAnimation();

            if (mute < 0)
            {
                this.music.stop();
                this.music = null;
                return;
            }

            this.music.volume(0.2f * mute);
        }
    }

    public AudioEngine getAudioEngine()
    {
        return this.audioEngine;
    }

    public void playSoundEffect(RandomAccessClip seekableAudioTrack, float x, float y, float volume)
    {
        var soundEffect = new SoundEffect(seekableAudioTrack, new Vector3f(x, y, 0), volume);
        this.audioEngine.playSound(soundEffect);
    }

    public void playSoundEffect(RandomAccessClip seekableAudioTrack, float x, float y, float volume, float pitch)
    {
        var soundEffect = new SoundEffect(seekableAudioTrack, new Vector3f(x, y, 0), volume);
        soundEffect.pitch(pitch);
        this.audioEngine.playSound(soundEffect);
    }
}
