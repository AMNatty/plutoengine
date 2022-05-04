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

package cz.tefek.srclone.ammo;

import org.plutoengine.audio.RandomAccessClip;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.entity.projectile.*;

public enum EnumAmmo
{
    P_LASER_BEAM("☀", "laser", 0.2f, EnumTeam.PLAYER, EntityProjectilePlayerLaserBeam::new),
    P_HEAT_STAR("\uD83C\uDF20", "heatStar", 0.4f, EnumTeam.PLAYER, EntityProjectilePlayerHeatStar::new),
    P_ELECTRON_FLARE("\uD83D\uDCA0", "electronFlare", 2.5f, EnumTeam.PLAYER, EntityProjectilePlayerElectronFlare::new),
    P_POLY_SWARM("\uD83D\uDD3A", "polySwarm", 0.1f, EnumTeam.PLAYER, EntityProjectilePlayerPolySwarm::new),
    P_PLASMA_DISC("\uD83D\uDFE2", "plasma", 2.5f, EnumTeam.PLAYER, EntityProjectilePlayerPlasmaDisc::new),
    P_TACHYON_DISC("\uD836\uDD53", "tachyon", 2.0f, EnumTeam.PLAYER, EntityProjectilePlayerTachyonDisc::new),


    E_LASER_BEAM("☀", "laser", 2.0f, EnumTeam.ENEMY, EntityProjectileEnemyLaserBeam::new),

    E_HEAT_STAR("\uD83C\uDF20", "heatStar", 3.0f, EnumTeam.ENEMY, EntityProjectileEnemyHeatStar::new);

    private final EnumTeam team;

    private final String shootSound;

    private final float cooldown;

    private final String textIcon;

    private final Supplier<EntityProjectileAmmo> projectileSupplier;

    EnumAmmo(String textIcon, String shootSound, float cooldown, EnumTeam team, Supplier<EntityProjectileAmmo> projectileCreateFunc)
    {
        this.textIcon = textIcon;
        this.shootSound = shootSound;
        this.cooldown = cooldown;
        this.team = team;
        this.projectileSupplier = projectileCreateFunc;
    }

    private static final List<EnumAmmo> PLAYER_SELECTABLE = Arrays.stream(EnumAmmo.values())
                                                                  .filter(EnumAmmo::isPlayerSelectable)
                                                                  .toList();

    public static final long AMMO_INFINITE = Long.MAX_VALUE;

    public static EnumAmmo next(EnumAmmo ammo)
    {
        int idx = PLAYER_SELECTABLE.indexOf(ammo);
        return PLAYER_SELECTABLE.get((idx + 1) % PLAYER_SELECTABLE.size());
    }

    public static EnumAmmo previous(EnumAmmo ammo)
    {
        int idx = PLAYER_SELECTABLE.indexOf(ammo);
        int size = PLAYER_SELECTABLE.size();
        return PLAYER_SELECTABLE.get((idx - 1 + size) % size);
    }

    public float getCooldown()
    {
        return this.cooldown;
    }

    public RandomAccessClip getShootSound()
    {
        return SRCloneMod.shootSounds.get(this.shootSound);
    }

    public EntityProjectileAmmo createProjectile()
    {
        return this.projectileSupplier.get();
    }

    public boolean isPlayerSelectable()
    {
        return this.team == EnumTeam.PLAYER;
    }

    public String getTextIcon()
    {
        return this.textIcon;
    }
}
