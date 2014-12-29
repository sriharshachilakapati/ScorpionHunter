package com.shc.scorpionhunter;

import com.shc.scorpionhunter.entities.Bullet;
import com.shc.scorpionhunter.entities.Scorpion;
import com.shc.scorpionhunter.entities.Shooter;
import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.collision.QuadTreeSceneCollider;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.utils.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class ScorpionHunter extends Game
{
    static
    {
        development = true;
    }

    public static  OrthoCam CAMERA;
    private static OrthoCam hudCam;

    public static Texture SHOOTER_STANDING;
    public static Texture SHOOTER_SHOOTING;
    public static Texture SCORPION_NORMAL;
    public static Texture SCORPION_FROZEN;
    public static Texture BULLET;
    public static Texture BLOOD;
    public static Texture SAND_BG;

    public static Scene   gameScene;
    public static Shooter shooter;

    public static Sound MUSIC;
    public static Sound SHOOT;
    public static Sound HURT;

    private QuadTreeSceneCollider collider;

    public static int score;
    public static int health;

    private TrueTypeFont font;
    private TrueTypeFont largeFont;

    private float     minSecondsToNextSpawn;
    private GameTimer spawnTimer;

    private boolean startGame;
    private boolean paused;

    public void init()
    {
        CAMERA = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());
        hudCam = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());

        ResourceLoader loader = ResourceLoader.getInstance();

        int spriteSheetID = loader.defineTexture("resources/spritesheet.png");

        int fontID = loader.defineFont("resources/Punk_s_not_dead.ttf", TrueTypeFont.STYLE_NORMAL, 48);
        int fontID2 = loader.defineFont("resources/Punk_s_not_dead.ttf", TrueTypeFont.STYLE_NORMAL, 128);

        int musicID = loader.defineSound("resources/music.ogg");
        int shootID = loader.defineSound("resources/shoot.wav");
        int hurtID  = loader.defineSound("resources/hurt.wav");

        loader.startLoading();

        SpriteSheet spriteSheet;
        spriteSheet = new SpriteSheet(loader.getTexture(spriteSheetID), 128, 128);

        SHOOTER_STANDING = spriteSheet.getCell(0, 0);
        SHOOTER_SHOOTING = spriteSheet.getCell(0, 1);
        SCORPION_NORMAL  = spriteSheet.getCell(0, 2);
        SCORPION_FROZEN  = spriteSheet.getCell(0, 3);
        BULLET           = spriteSheet.getCell(1, 0);
        BLOOD            = spriteSheet.getCell(1, 1);
        SAND_BG          = spriteSheet.getCell(1, 2);

        // Bullet needs to be small than the normal textures
        float minU = (BULLET.getWidth()/2 - 8f) / BULLET.getWidth();
        float maxU = (BULLET.getWidth()/2 + 8f) / BULLET.getWidth();
        float minV = (BULLET.getHeight()/2 - 20.5f) / BULLET.getHeight();
        float maxV = (BULLET.getHeight()/2 + 20.5f) / BULLET.getHeight();

        BULLET = BULLET.getSubTexture(minU, minV, maxU, maxV);

        font = loader.getFont(fontID);
        largeFont = loader.getFont(fontID2);

        MUSIC = loader.getSound(musicID);
        SHOOT = loader.getSound(shootID);
        HURT  = loader.getSound(hurtID);

        MUSIC.setLooping(true);
        MUSIC.play();

        initGameScene();
    }

    private void initGameScene()
    {
        gameScene = new Scene();
        shooter = new Shooter(new Vector2(100, 100));
        gameScene.addChild(shooter);
        gameScene.init();

        health = 100;

        collider = new QuadTreeSceneCollider(1920, 1080);
        collider.setScene(gameScene);

        collider.register(Shooter.class,  Scorpion.class);
        collider.register(Bullet.class,   Scorpion.class);
        collider.register(Scorpion.class, Scorpion.class);

        spawnTimer = new GameTimer(10, TimeUtils.Unit.SECONDS);
        spawnTimer.setCallback(this::spawnScorpion);
        minSecondsToNextSpawn = 10;
    }

    public void update(float delta)
    {
        Display.setTitle("ScorpionHunter");

        if ((paused || health == 0 || !startGame) && Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();

        if (!startGame && Keyboard.isClicked(Keyboard.KEY_SPACE))
        {
            startGame = true;
            spawnScorpion();
            return;
        }

        if (startGame && health != 0 && Keyboard.isClicked(Keyboard.KEY_ESCAPE) && !paused)
        {
            paused = true;
            return;
        }

        if (paused && Keyboard.isClicked(Keyboard.KEY_SPACE))
        {
            paused = false;
            return;
        }

        if (health == 0 && Keyboard.isClicked(Keyboard.KEY_SPACE))
        {
            health = 100;
            score  = 0;
            initGameScene();
            spawnScorpion();

            return;
        }

        if (health == 0 || !startGame || paused)
            return;

        gameScene.update(delta);
        collider.checkCollisions();

        health = MathUtils.clamp(health, 0, 100);
    }

    private void spawnScorpion()
    {
        float x = MathUtils.random(2) == 0 ? -shooter.getCenter().x - Display.getWidth()/2
                                           : shooter.getCenter().x + Display.getWidth()/2;

        float y = MathUtils.random(2) == 0 ? -shooter.getCenter().y - Display.getHeight()/2
                                           : shooter.getCenter().y + Display.getHeight()/2;

        Vector2 position = new Vector2(x, y);
        float velocity = - (10 - minSecondsToNextSpawn);

        velocity = MathUtils.clamp(velocity, -4, -1);

        gameScene.addChild(new Scorpion(position, velocity));

        minSecondsToNextSpawn -= 0.005f;
        minSecondsToNextSpawn = MathUtils.clamp(minSecondsToNextSpawn, 2, 10);

        spawnTimer.setTime(minSecondsToNextSpawn, TimeUtils.Unit.SECONDS);
        spawnTimer.start();
    }

    public void render(float delta, Batcher batcher)
    {
        hudCam.apply();

        for (int i = 0; i < Display.getWidth(); i += 127)
        {
            for (int j = 0; j < Display.getHeight(); j += 127)
            {
                batcher.drawTexture2d(SAND_BG, new Vector2(i, j));
            }
        }

        CAMERA.apply();

        gameScene.render(delta, batcher);

        hudCam.apply();
        font.drawString(batcher, "FPS: " + getFPS() + " | UPS: " + getUPS(), 10, 10, Color.DARK_RED);

        String scoreString = "Score: " + score;
        int width = font.getWidth(scoreString);
        font.drawString(batcher, scoreString, Display.getWidth() - width - 10, 10, Color.BLUE);

        String healthString = "\nHealth: " + health;
        font.drawString(batcher, healthString, 10, 10, Color.DARK_GREEN);

        Color gray = Color.BLACK.copy();
        gray.setAlpha(0.9f);

        if (!startGame || health == 0 || paused)
        {
            batcher.begin(Primitive.TRIANGLE_STRIP);
            {
                batcher.vertex(0, 0);
                batcher.color(gray);

                batcher.vertex(Display.getWidth(), 0);
                batcher.color(gray);

                batcher.vertex(0, Display.getHeight());
                batcher.color(gray);

                batcher.vertex(Display.getWidth(), Display.getHeight());
                batcher.color(gray);
            }
            batcher.end();

            String messageString1;
            String messageString2;

            if (!startGame)
            {
                messageString1 = "ScorpionHunter";
                messageString2 = "Press SPACE to PLAY";
            }
            else if (health == 0)
            {
                messageString1 = "You are DEAD!";
                messageString2 = "You scored " + score + "\nPress SPACE to try again!";
            }
            else
            {
                messageString1 = "PAUSED";
                messageString2 = "Press SPACE to RESUME\nESCAPE to QUIT";
            }

            float x = Display.getWidth() / 2 - largeFont.getWidth(messageString1) / 2;
            float y = Display.getHeight() / 2 - largeFont.getHeight() / 2 - 20;

            largeFont.drawString(batcher, messageString1, x, y, Color.random());

            x = Display.getWidth() / 2 - font.getWidth(messageString2) / 2;
            y = Display.getHeight() / 2 + font.getHeight() / 2 + 20;

            font.drawString(batcher, messageString2, x, y, Color.WHITE);
        }
    }

    public void resize()
    {
        CAMERA.initProjection(Display.getWidth(), Display.getHeight());
        hudCam.initProjection(Display.getWidth(), Display.getHeight());
    }

    public void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }

    public static void main(String[] args)
    {
        new ScorpionHunter().start();
    }
}
