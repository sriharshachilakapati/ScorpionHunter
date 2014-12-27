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
public class Scorpion extends Entity2D
{
    private boolean frozen;
    private float   velocity;

    private GameTimer freezeTimer;

    public Scorpion(Vector2 position, float velocity)
    {
        super(new Rectangle(0, 0, 128, 128));
        setPosition(position);

        frozen = false;

        freezeTimer = new GameTimer(3, TimeUtils.Unit.SECONDS);
        freezeTimer.setCallback(() -> frozen = false);

        this.velocity = velocity;
    }

    public void update(float delta)
    {
        if (frozen)
        {
            setVelocity(Vector2.ZERO);
            return;
        }

        frozen = false;

        Vector2 direction = getCenter().subtract(ScorpionHunter.shooter.getCenter());
        setRotation((float) Math.toDegrees(Math.atan2(direction.y, direction.x)) - 90);

        float sinAngle = (float) Math.sin(Math.toRadians(getRotation() + 90));
        float cosAngle = (float) Math.cos(Math.toRadians(getRotation() + 90));

        setVelocity(new Vector2(cosAngle, sinAngle).scale(velocity));
    }

    public void collision(Entity2D other)
    {
        if (other instanceof Scorpion)
        {
            other.alignNextTo(this);
        }
    }

    public void render(float delta, Batcher batcher)
    {
        batcher.applyTransform(getTransform());
        if (frozen)
            batcher.drawTexture2d(ScorpionHunter.SCORPION_FROZEN, Vector2.ZERO);
        else
            batcher.drawTexture2d(ScorpionHunter.SCORPION_NORMAL, getVelocity().scale(delta));
    }

    public void freeze()
    {
        frozen = true;
        freezeTimer.start();
    }

    public boolean isFrozen()
    {
        return frozen;
    }
}
