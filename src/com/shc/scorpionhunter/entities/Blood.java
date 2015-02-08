package com.shc.scorpionhunter.entities;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.math.Vector2;

/**
 * Represents a Blood entity in the scene.
 *
 * @author Sri Harsha Chilakapati
 */
public class Blood extends Entity2D
{
    // The life is the opacity of the blood instance
    private float life = 1.0f;

    /**
     * Creates some Blood at a position
     *
     * @param position The position of the blood
     */
    public Blood(Vector2 position)
    {
        setPolygon(new Rectangle(0, 0, 128, 128));
        setPosition(position);
    }

    /**
     * This method is called to update this entity. This is where the direction
     * and other properties get calculated and the velocity is changed.
     *
     * @param delta The time span of a single frame in seconds
     */
    public void update(float delta)
    {
        // Decrease the life
        life -= delta;

        // If there is no more life, destroy
        if (life < 0)
            destroy();
    }

    /**
     * This method is called whenever this entity needs to be redrawn.
     *
     * @param delta   The lag between frames ranging from 0 to 1
     * @param batcher The Batcher instance used to render to the screen
     */
    public void render(float delta, Batcher batcher)
    {
        Color color = Color.TRANSPARENT.copy();
        color.setAlpha(life);

        // It is not possible to tint a texture using Graphics2D
        batcher.applyTransform(getTransform());
        batcher.drawTexture2d(Resources.BLOOD, Vector2.ZERO, color);
    }
}
