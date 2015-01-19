package com.shc.scorpionhunter.states;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.scorpionhunter.entities.Bullet;
import com.shc.scorpionhunter.entities.Scorpion;
import com.shc.scorpionhunter.entities.Shooter;
import com.shc.silenceengine.collision.colliders.DynamicSceneCollider2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
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
        GAME_SCENE.addChild(shooter = new Shooter(new Vector2(Display.getWidth()/2, Display.getHeight()/2)));
        GAME_SCENE.init();

        collider = new DynamicSceneCollider2D();
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
        // Only spawn if this is the current state
        if (ScorpionHunter.CURRENT_STATE != this)
            return;

        // Spawn a scorpion
        float x = MathUtils.random(2) == 0 ? -shooter.getCenter().x - Display.getWidth()/2
                                           : shooter.getCenter().x + Display.getWidth()/2;

        float y = MathUtils.random(2) == 0 ? -shooter.getCenter().y - Display.getHeight()/2
                                           : shooter.getCenter().y + Display.getHeight()/2;

        Vector2 position = new Vector2(x, y);

        GAME_SCENE.addChild(new Scorpion(position, 2));

        // Speed up spawning from next time
        spawnTime = MathUtils.clamp(spawnTime - 0.005f, 7, 10);
        spawnTimer.setTime(spawnTime, TimeUtils.Unit.SECONDS);
        spawnTimer.start();
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            ScorpionHunter.CURRENT_STATE = ScorpionHunter.PAUSE_STATE;

        if (ScorpionHunter.HEALTH <= 0)
            ScorpionHunter.CURRENT_STATE = ScorpionHunter.GAMEOVER_STATE;

        GAME_SCENE.update(delta);
        collider.checkCollisions();
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Resources.CAMERA.apply();

        final Texture background = Resources.SAND_BG;

        final int bgWidth = (int) background.getWidth();
        final int bgHeight = (int) background.getHeight();

        final int displayWidth  = Display.getWidth();
        final int displayHeight = Display.getHeight();

        final int startX = (int) Math.floor(shooter.getCenter().x / bgWidth) * bgWidth - displayWidth / 2;
        final int startY = (int) Math.floor(shooter.getCenter().y / bgHeight) * bgHeight - displayHeight / 2;
        final int endX   = (int) (Display.getWidth()/2 + shooter.getCenter().x);
        final int endY   = (int) (Display.getHeight()/2 + shooter.getCenter().y);

        for (int x = startX; x < endX; x += bgWidth)
        {
            for (int y = startY; y < endY; y += bgHeight)
            {
                batcher.drawTexture2d(Resources.SAND_BG, new Vector2(x, y));
            }
        }

        GAME_SCENE.render(delta, batcher);

        Resources.HUD_CAMERA.apply();

        Resources.HUD_FONT.drawString(batcher, "FPS: " + Game.getFPS() + " | UPS: " + Game.getUPS(), 10, 10, Color.DARK_RED);

        String scoreString = "Score: " + ScorpionHunter.SCORE;
        int width = Resources.HUD_FONT.getWidth(scoreString);
        Resources.HUD_FONT.drawString(batcher, scoreString, ScorpionHunter.CANVAS_WIDTH - width - 10, 10, Color.BLUE);

        String healthString = "\nHealth: " + ScorpionHunter.HEALTH;
        Resources.HUD_FONT.drawString(batcher, healthString, 10, 10, Color.DARK_GREEN);
    }
}
