package com.shc.scorpionhunter.entities;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.states.PlayState;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * Represents the Scorpion Entity in the scene.
 *
 * @author Sri Harsha Chilakapati
 */
public class Scorpion extends Entity2D
{
    // Some private variables
    private boolean frozen;
    private float   velocity;

    // The freeze timer used to wake up this scorpion from freeze
    private GameTimer freezeTimer;

    /**
     * Constructs a Scorpion at a position with a velocity
     *
     * @param position The initial position
     * @param velocity The initial velocity
     */
    public Scorpion(Vector2 position, float velocity)
    {
        super(new Rectangle(0, 0, 128, 128));
        setPosition(position);

        // Don't freeze on the start
        frozen = false;

        // Create a wake up timer
        freezeTimer = new GameTimer(3, TimeUtils.Unit.SECONDS);
        freezeTimer.setCallback(() -> frozen = false);

        // Use negative velocity, the texture is pointing in the opposite direction
        this.velocity = -velocity;
    }

    /**
     * This method is called to update this entity. This is where the direction
     * and other properties get calculated and the velocity is changed.
     *
     * @param delta The time span of a single frame in seconds
     */
    public void update(float delta)
    {
        if (frozen)
        {
            setVelocity(Vector2.ZERO);
            return;
        }

        frozen = false;

        // Calculate the direction to the shooter (Basic AI!!)
        Vector2 direction = getCenter().subtract(PlayState.shooter.getCenter());

        float rotation = MathUtils.atan2(direction.y, direction.x) - 90;
        setRotation(rotation);

        // Calculate the angles
        float sinAngle = MathUtils.sin(rotation + 90);
        float cosAngle = MathUtils.cos(rotation + 90);

        Vector2 velocity = new Vector2(cosAngle, sinAngle).scale(this.velocity);

        // Set the velocity
        setVelocity(velocity);
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
            other.alignNextTo(this);
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
        batcher.applyTransform(getTransform());
        if (frozen)
            batcher.drawTexture2d(Resources.SCORPION_FROZEN, Vector2.ZERO);
        else
            batcher.drawTexture2d(Resources.SCORPION_NORMAL, getVelocity().scale(delta));
    }

    /**
     * Freezes this scorpion entity.
     */
    public void freeze()
    {
        frozen = true;
        freezeTimer.start();
    }

    /**
     * @return True if frozen, else false
     */
    public boolean isFrozen()
    {
        return frozen;
    }
}
