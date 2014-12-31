package com.shc.scorpionhunter.entities;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.scorpionhunter.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Shooter extends Entity2D
{
    private Texture texture;

    private boolean canShoot;
    private GameTimer shootTimer;

    public Shooter(Vector2 position)
    {
        super(new Rectangle(0, 0, 128, 128));

        texture = Resources.SHOOTER_STANDING;

        canShoot = true;
        setPosition(position);

        shootTimer = new GameTimer(0.75, TimeUtils.Unit.SECONDS);
        shootTimer.setCallback(this::shootTimeOut);
    }

    public void update(float delta)
    {
        float velocity = 0;

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            rotate(-delta * 90);

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            rotate(delta * 90);

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            velocity = -4;

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            velocity = 4;

        float sinAngle = (float) Math.sin(Math.toRadians(getRotation() + 90));
        float cosAngle = (float) Math.cos(Math.toRadians(getRotation() + 90));

        setVelocity(new Vector2(cosAngle, sinAngle).scale(velocity));

        if (Keyboard.isClicked(Keyboard.KEY_SPACE) && canShoot)
        {
            shootTimer.start();
            texture = Resources.SHOOTER_SHOOTING;
            canShoot = false;
            PlayState.GAME_SCENE.addChild(new Bullet(getCenter().add(getVelocity()), getRotation()));

            Resources.SHOOT.play();
        }
    }

    public void collision(Entity2D other)
    {
        if (other instanceof Scorpion)
        {
            other.alignNextTo(this);

            if (!((Scorpion) other).isFrozen())
            {
                PlayState.GAME_SCENE.addChild(new Blood(getPosition()));
                ScorpionHunter.HEALTH -= 5;

                Resources.HURT.play();
            }
        }
    }

    private void shootTimeOut()
    {
        canShoot = true;
        texture = Resources.SHOOTER_STANDING;
    }

    public void render(float delta, Batcher batcher)
    {
        batcher.applyTransform(getTransform());
        batcher.drawTexture2d(texture, getVelocity().scale(delta));
    }
}
