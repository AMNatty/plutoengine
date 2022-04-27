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
