package cz.tefek.srclone.entity.pickup;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.entity.Entity;
import cz.tefek.srclone.entity.EntityPlayer;

public class EntityPickup extends Entity
{
    protected EntityPickup()
    {
        this.team = EnumTeam.PLAYER;
        this.collision = false;
    }

    @Override
    public void tick(float delta)
    {
        var player = this.game.getEntityPlayer();

        if (this.collides(player))
        {
            this.onPickup(player);
            this.deadFlag = true;
        }

        super.tick(delta);
    }

    protected void onPickup(EntityPlayer player)
    {

    }
}
