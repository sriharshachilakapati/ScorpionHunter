package com.shc.scorpionhunter.states;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.scorpionhunter.entities.Bullet;
import com.shc.scorpionhunter.entities.Scorpion;
import com.shc.scorpionhunter.entities.Shooter;
import com.shc.silenceengine.collision.QuadTreeSceneCollider;
import com.shc.silenceengine.collision.SceneCollider2D;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.utils.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    public static final int WIDTH  = 1920;
    public static final int HEIGHT = 1080;

    public static Scene GAME_SCENE;
    private SceneCollider2D collider;

    private GameTimer spawnTimer;
    private float     spawnTime;

    public static Shooter shooter;

    public PlayState()
    {
        ScorpionHunter.SCORE = 0;
        ScorpionHunter.HEALTH = 100;

        GAME_SCENE = new Scene();
        GAME_SCENE.addChild(shooter = new Shooter(new Vector2(WIDTH/2, HEIGHT/2)));
        GAME_SCENE.init();

        collider = new QuadTreeSceneCollider(WIDTH, HEIGHT);
        collider.setScene(GAME_SCENE);

        collider.register(Shooter.class,  Scorpion.class);
        collider.register(Bullet.class,   Scorpion.class);
        collider.register(Scorpion.class, Scorpion.class);

        spawnTime = 10;

        spawnTimer = new GameTimer(spawnTime, TimeUtils.Unit.SECONDS);
        spawnTimer.setCallback(this::spawnScorpion);
        spawnTimer.start();
    }

    private void spawnScorpion()
    {
        // Only run if this is the current state
        if (ScorpionHunter.STATE != this)
            return;

        // Spawn a scorpion
        float x = MathUtils.random(2) == 0 ? -shooter.getCenter().x - Display.getWidth()/2
                                           : shooter.getCenter().x + Display.getWidth()/2;

        float y = MathUtils.random(2) == 0 ? -shooter.getCenter().y - Display.getHeight()/2
                                           : shooter.getCenter().y + Display.getHeight()/2;

        x = MathUtils.clamp(x, 0, WIDTH - 128);
        y = MathUtils.clamp(y, 0, HEIGHT - 128);

        Vector2 position = new Vector2(x, y);

        GAME_SCENE.addChild(new Scorpion(position, 2));

        // Speed up spawning from next time
        spawnTime = MathUtils.clamp(spawnTime - 0.005f, 7, 10);
        spawnTimer.setTime(spawnTime, TimeUtils.Unit.SECONDS);
        spawnTimer.start();

        System.out.println("Scorpion spawned at " + position + ". Next spawn at " + spawnTime);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            ScorpionHunter.STATE = ScorpionHunter.PAUSE_STATE;

        if (ScorpionHunter.HEALTH <= 0)
            ScorpionHunter.STATE = ScorpionHunter.GAMEOVER_STATE;

        GAME_SCENE.update(delta);
        collider.checkCollisions();

        // Center the player in the scene
        Vector2 center = shooter.getCenter().copy();

        center.x = MathUtils.clamp(center.x, Display.getWidth()/2, WIDTH - Display.getWidth()/2);
        center.y = MathUtils.clamp(center.y, Display.getHeight()/2, HEIGHT - Display.getHeight()/2);

        Resources.CAMERA.center(center);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Resources.CAMERA.apply();

        for (int x = 0; x < WIDTH; x += 127)
        {
            for (int y = 0; y < HEIGHT; y += 127)
            {
                batcher.drawTexture2d(Resources.SAND_BG, new Vector2(x, y));
            }
        }

        GAME_SCENE.render(delta, batcher);

        Resources.HUD_CAMERA.apply();

        Resources.HUD_FONT.drawString(batcher, "FPS: " + Game.getFPS() + " | UPS: " + Game.getUPS(), 10, 10, Color.DARK_RED);

        String scoreString = "Score: " + ScorpionHunter.SCORE;
        int width = Resources.HUD_FONT.getWidth(scoreString);
        Resources.HUD_FONT.drawString(batcher, scoreString, Display.getWidth() - width - 10, 10, Color.BLUE);

        String healthString = "\nHealth: " + ScorpionHunter.HEALTH;
        Resources.HUD_FONT.drawString(batcher, healthString, 10, 10, Color.DARK_GREEN);
    }
}
