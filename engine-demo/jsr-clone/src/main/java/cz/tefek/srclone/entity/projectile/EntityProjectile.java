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
