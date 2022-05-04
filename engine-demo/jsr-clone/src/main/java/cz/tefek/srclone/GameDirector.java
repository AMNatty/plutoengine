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

package cz.tefek.srclone;

import org.plutoengine.PlutoLocal;
import org.plutoengine.display.Display;
import org.plutoengine.logger.Logger;

import java.util.Random;

import cz.tefek.srclone.entity.EntityPlayer;
import cz.tefek.srclone.entity.enemy.EntityEnemy;
import cz.tefek.srclone.entity.enemy.EntityEnemyScout;
import cz.tefek.srclone.entity.enemy.EntityEnemySmallBomber;

public class GameDirector
{
    private double gameTime;

    private double spawnTimer;

    private final Game game;

    private double difficulty;

    GameDirector(Game game)
    {
        this.game = game;
    }

    public void tick(double delta)
    {
        var rand = this.game.getRandom();

        this.spawnTimer -= delta;

        if (this.spawnTimer <= 0)
        {
            this.difficulty = 10 + Math.pow(this.gameTime, 1.5f);

            double spawnTimerNext = 160 / (4 + Math.log10(difficulty));

            double variation = 0.75f + rand.nextDouble() / 2.0;
            double rest = (1.0f + Math.sin(difficulty / 120.0) / 4.0);
            double difficultyModifier = Math.sqrt(difficulty / 25.0f);
            int waveSize = (int) (1 + difficultyModifier * variation * rest);

            Logger.logf("Wave size: %d%n", waveSize);
            Logger.logf("Next wave in: %.0f seconds%n", spawnTimerNext);
            Logger.log("---------------");

            var player = this.game.getEntityPlayer();

            this.spawn(player, rand, waveSize);

            this.spawnTimer += spawnTimerNext;
        }

        this.gameTime += delta;
    }

    public double getDifficulty()
    {
        return this.difficulty;
    }

    private void spawn(EntityPlayer player, Random rand, int budget)
    {
        var display = PlutoLocal.components().getComponent(Display.class);

        double minSpawnRadius = 200 + Math.hypot(display.getWidth(), display.getHeight()); // HACK: Spawn enemies just outside the visible radius

        float px = player.getX();
        float py = player.getY();

        double tdir = rand.nextDouble() * 2 * Math.PI;

        for (int i = 0; i < budget; i++)
        {
            EntityEnemy enemy;

            int enemyTypes = 2;

            switch (rand.nextInt(enemyTypes))
            {
                case 1:
                    if (budget >= 5)
                    {
                        enemy = new EntityEnemySmallBomber();
                        i += 2;
                        break;
                    }
                case 0:
                default:
                    enemy = new EntityEnemyScout();
            }

            double varDir = (rand.nextDouble() - 0.5) * (Math.PI / 8);

            double t = tdir + varDir;
            double x = px + Math.cos(t) * minSpawnRadius;
            double y = py + Math.sin(t) * minSpawnRadius;

            this.game.addEntity(enemy, (float) x, (float) y);
        }
    }
}
