package cz.tefek.srclone.entity.enemy;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.entity.EntityLiving;

public abstract class EntityEnemy extends EntityLiving
{
    protected EntityEnemy()
    {
        this.team = EnumTeam.ENEMY;
    }

    @Override
    public void onDie()
    {
        var player = this.game.getEntityPlayer();
        player.addScore(this.maxHealth);

        super.onDie();
    }
}
