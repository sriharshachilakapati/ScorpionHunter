package com.shc.scorpionhunter.entities;

import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class Blood extends Entity2D
{
    private float life = 1.0f;

    public Blood(Vector2 position)
    {
        setPolygon(new Rectangle(0, 0, 128, 128));
        setPosition(position);
    }

    public void update(float delta)
    {
        life -= delta;

        if (life < 0)
            destroy();
    }

    public void render(float delta, Batcher batcher)
    {
        Color color = Color.TRANSPARENT.copy();
        color.setAlpha(life);

        batcher.applyTransform(getTransform());
        batcher.drawTexture2d(ScorpionHunter.BLOOD, Vector2.ZERO, color);
    }
}
