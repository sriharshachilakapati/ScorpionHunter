package com.shc.scorpionhunter.entities;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.scorpionhunter.states.PlayState;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.*;

/**
 * Represents the Bullet in the Game.
 *
 * @author Sri Harsha Chilakapati
 */
public class Bullet extends Entity2D
{
    /**
     * Constructs a bullet at a position and with an angle.
     *
     * @param center   The center of this bullet
     * @param rotation The rotation of this bullet
     */
    public Bullet(Vector2 center, float rotation)
    {
        super(new Rectangle(0, 0, 16, 41));

        // Set the position and apply the rotation
        setPosition(center);
        setRotation(rotation);

        // The bullet of the velocity
        float velocity = -8;

        // Calculate the angles
        float sinAngle = MathUtils.sin(getRotation() + 90);
        float cosAngle = MathUtils.cos(getRotation() + 90);

        // Calculate the velocity
        getVelocity().set(cosAngle, sinAngle).scaleSelf(velocity);

        // Start the death timer, will only have 5 seconds of life
        GameTimer timer = new GameTimer(5, TimeUtils.Unit.SECONDS);
        timer.setCallback(this::timerOut);
        timer.start();
    }

    // The timer callback method
    private void timerOut()
    {
        destroy();
    }

    /**
     * This method is called by the SceneCollider2D whenever an entity
     * which was of a registered type collides this entity.
     *
     * @param other The other entity that was collided
     */
    public void collision(Entity2D other)
    {
        if (other instanceof Scorpion)
        {
            Scorpion scorpion = (Scorpion) other;

            if (scorpion.isFrozen())
            {
                // Destroy the scorpion and add some blood
                scorpion.destroy();
                PlayState.GAME_SCENE.addChild(new Blood(scorpion.getPosition()));
                ScorpionHunter.SCORE += 10;

                // Play the hurt sound
                Resources.HURT.play();
            }
            else
                scorpion.freeze();

            // Destroy self
            destroy();
        }
    }

    /**
     * This method is called whenever this entity needs to be redrawn.
     *
     * @param delta   The lag between frames ranging from 0 to 1
     * @param batcher The Batcher instance used to render to the screen
     */
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = Game.getGraphics2D();

        g2d.transform(getTransform());
        g2d.drawTexture(Resources.BULLET, getVelocity().scale(delta));
        g2d.resetTransform();
    }
}
