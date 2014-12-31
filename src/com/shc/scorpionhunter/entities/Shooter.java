package com.shc.scorpionhunter.entities;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.scorpionhunter.states.PlayState;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.*;

/**
 * This class represents the Shooter entity.
 *
 * @author Sri Harsha Chilakapati
 */
public class Shooter extends Entity2D
{
    // The texture of the shooter
    private Texture texture;

    // Some private variables
    private boolean canShoot;
    private GameTimer shootTimer;

    /**
     * Constructs a Shooter instance at a position
     *
     * @param position The initial position
     */
    public Shooter(Vector2 position)
    {
        // Construct the bounding box
        super(new Rectangle(0, 0, 128, 128));
        setPosition(position);

        // Default texture is standing
        texture = Resources.SHOOTER_STANDING;

        // By default, we can shoot
        canShoot = true;

        // Initialize the shoot timer
        shootTimer = new GameTimer(0.75, TimeUtils.Unit.SECONDS);
        shootTimer.setCallback(this::shootTimeOut);
    }

    /**
     * This method is called to update this entity. This is where the direction
     * and other properties get calculated and the velocity is changed.
     *
     * @param delta The time span of a single frame in seconds
     */
    public void update(float delta)
    {
        // By default, the velocity should be zero
        float velocity = 0;

        // Handle rotations
        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            rotate(-delta * 90);

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            rotate(delta * 90);

        // Handle movement
        if (Keyboard.isPressed(Keyboard.KEY_UP))
            velocity = -4;

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            velocity = 4;

        // Calculate the velocity based on the direction we are moving
        float sinAngle = (float) Math.sin(Math.toRadians(getRotation() + 90));
        float cosAngle = (float) Math.cos(Math.toRadians(getRotation() + 90));

        setVelocity(new Vector2(cosAngle, sinAngle).scale(velocity));

        if (Keyboard.isClicked(Keyboard.KEY_SPACE) && canShoot)
        {
            // Start the shoot timer
            shootTimer.start();
            texture = Resources.SHOOTER_SHOOTING;
            canShoot = false;

            // Calculate the bullet position
            Vector2 bulletPosition = new Vector2(8, -34).rotate(getRotation()).add(getCenter());

            // Add the bullet to the scene
            PlayState.GAME_SCENE.addChild(new Bullet(bulletPosition, getRotation()));

            // Play the shoot sound
            Resources.SHOOT.play();
        }

        // Center in the screen
        Resources.CAMERA.center(getCenter());
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
            // Align other next to shooter (we don't want intersections)
            other.alignNextTo(this);

            if (!((Scorpion) other).isFrozen())
            {
                // Add some blood at the shooter position and decrease health
                PlayState.GAME_SCENE.addChild(new Blood(getPosition()));
                ScorpionHunter.HEALTH -= 5;

                // Play the hurt sound
                Resources.HURT.play();
            }
        }
    }

    // This is the callback method that was attached to shoot timer
    private void shootTimeOut()
    {
        canShoot = true;
        texture = Resources.SHOOTER_STANDING;
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
        batcher.drawTexture2d(texture, getVelocity().scale(delta));
    }
}
