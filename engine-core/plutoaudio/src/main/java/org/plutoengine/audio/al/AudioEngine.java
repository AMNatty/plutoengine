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
