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

package cz.tefek.srclone.entity.projectile;

import cz.tefek.srclone.entity.Entity;

public abstract class EntityProjectile extends Entity
{
    protected EntityProjectile()
    {

    }

    public void tick(float delta)
    {
        switch (this.team)
        {
            case ENEMY -> {
                var player = this.game.getEntityPlayer();

                if (this.collides(player))
                    this.onHit(player);
            }

            case PLAYER -> {
                var collision = this.game.getEntities()
                                         .parallelStream()
                                         .filter(Entity::hasCollision)
                                         .filter(this::collides)
                                         .iterator();

                while (collision.hasNext() && !this.deadFlag)
                    this.onHit(collision.next());
            }
        }

        super.tick(delta);
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    protected void onHit(Entity entity)
    {

    }
}
