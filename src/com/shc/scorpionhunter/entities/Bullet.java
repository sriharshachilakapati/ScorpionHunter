package com.shc.scorpionhunter.entities;

import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Bullet extends Entity2D
{
    public Bullet(Vector2 center, float rotation)
    {
        super(new Rectangle(64, 0, 16, 41));

        setPosition(center);
        setRotation(rotation);

        float velocity = -8;

        float sinAngle = (float) Math.sin(Math.toRadians(getRotation() + 90));
        float cosAngle = (float) Math.cos(Math.toRadians(getRotation() + 90));

        setVelocity(new Vector2(cosAngle, sinAngle).scale(velocity));

        GameTimer timer = new GameTimer(5, TimeUtils.Unit.SECONDS);
        timer.setCallback(this::timerOut);
        timer.start();
    }

    private void timerOut()
    {
        destroy();
    }

    public void collision(Entity2D other)
    {
        if (other instanceof Scorpion)
        {
            Scorpion scorpion = (Scorpion) other;

            if (scorpion.isFrozen())
            {
                scorpion.destroy();
                ScorpionHunter.gameScene.addChild(new Blood(scorpion.getPosition()));
                ScorpionHunter.score += 10;

                ScorpionHunter.HURT.play();
            }
            else
                scorpion.freeze();

            destroy();
        }
    }

    public void render(float delta, Batcher batcher)
    {
        batcher.applyTransform(getTransform());
        batcher.drawTexture2d(ScorpionHunter.BULLET, getVelocity().scale(delta));
    }
}
