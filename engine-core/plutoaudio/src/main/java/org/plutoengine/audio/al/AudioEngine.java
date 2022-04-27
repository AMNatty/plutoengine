package org.plutoengine.audio.al;

import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector3f;
import org.plutoengine.component.AbstractComponent;
import org.plutoengine.component.ComponentToken;
import org.plutoengine.component.PlutoLocalComponent;

import java.util.ArrayList;
import java.util.List;

public class AudioEngine extends PlutoLocalComponent
{
    public static final ComponentToken<AudioEngine> TOKEN = ComponentToken.create(AudioEngine::new);

    private AudioContext context;

    private final List<Pair<AudioClipSource, AudioSourceInfo>> sfx;


    private AudioEngine()
    {
        this.sfx = new ArrayList<>();
    }

    @Override
    protected void onMount(AbstractComponent<PlutoLocalComponent>.ComponentDependencyManager manager)
    {
        this.context = manager.declareDependency(ComponentToken.create(AudioContext::new));
    }

    public void update()
    {
        for (var iterator = this.sfx.listIterator(); iterator.hasNext(); )
        {
            var data = iterator.next();
            var source = data.getKey();
            var info = data.getValue();

            var kaFunc = info.keepAliveFunction();
            if (kaFunc != null && !kaFunc.getAsBoolean())
                source.close();

            var moveFunc = info.moveFunction();
            if (!source.isClosed() && moveFunc != null)
            {
                var prevPos = source.getPosition();
                var newPos = moveFunc.apply(prevPos);
                var velocity = newPos.sub(prevPos, new Vector3f());

                source.position(this.context, newPos);
                source.velocity(this.context, velocity);
            }

            if (source.updateOrClose())
                iterator.remove();
        }
    }

    public void playSound(SoundEffect sfx)
    {
        var soundEffect = new AudioClipSource(sfx.getClip());
        soundEffect.volume(sfx.getVolume());
        soundEffect.pitch(sfx.getPitch());
        soundEffect.position(this.context, sfx.getPosition());
        soundEffect.play();
        var info = new AudioSourceInfo(sfx.getMovementMapper(), sfx.getKeepAliveFunction());
        var data = Pair.of(soundEffect, info);
        this.sfx.add(data);
    }

    @Override
    protected void onUnmount()
    {

    }

    @Override
    public boolean isUnique()
    {
        return true;
    }

    public AudioContext getContext()
    {
        return this.context;
    }
}
