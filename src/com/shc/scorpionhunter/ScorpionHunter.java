package com.shc.scorpionhunter;

import com.shc.scorpionhunter.states.GameOverState;
import com.shc.scorpionhunter.states.GameState;
import com.shc.scorpionhunter.states.IntroState;
import com.shc.scorpionhunter.states.PauseState;
import com.shc.scorpionhunter.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.TrueTypeFont;

/**
 * @author Sri Harsha Chilakapati
 */
public class ScorpionHunter extends Game
{
    static
    {
        development = true;
    }

    public static GameState STATE;
    public static GameState INTRO_STATE;
    public static GameState PLAY_STATE;
    public static GameState PAUSE_STATE;
    public static GameState GAMEOVER_STATE;

    public static int SCORE;
    public static int HEALTH;

    public void init()
    {
        // Initialize the resources
        initResources();

        // Initialize the GameStates
        INTRO_STATE = new IntroState();
        PLAY_STATE  = new PlayState();
        PAUSE_STATE = new PauseState();
        GAMEOVER_STATE = new GameOverState();

        // Initially show the INTRO state
        STATE = INTRO_STATE;
    }

    private void initResources()
    {
        // Create the Cameras first
        Resources.CAMERA = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());
        Resources.HUD_CAMERA = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());

        // Get the ResourceLoader instance
        ResourceLoader loader = ResourceLoader.getInstance();

        // Define the spritesheet texture
        int spriteSheetID = loader.defineTexture("resources/spritesheet.png");

        // Define fonts
        int fontID = loader.defineFont("resources/Punk_s_not_dead.ttf", TrueTypeFont.STYLE_NORMAL, 48);
        int fontID2 = loader.defineFont("resources/Punk_s_not_dead.ttf", TrueTypeFont.STYLE_NORMAL, 128);

        // Define sounds
        int musicID = loader.defineSound("resources/music.ogg");
        int shootID = loader.defineSound("resources/shoot.wav");
        int hurtID = loader.defineSound("resources/hurt.wav");

        // Start the loading of resources
        loader.startLoading();

        // Get the loaded spritesheet
        SpriteSheet spriteSheet;
        spriteSheet = new SpriteSheet(loader.getTexture(spriteSheetID), 128, 128);

        // Get the textures from the spritesheet
        Resources.SHOOTER_STANDING = spriteSheet.getCell(0, 0);
        Resources.SHOOTER_SHOOTING = spriteSheet.getCell(0, 1);
        Resources.SCORPION_NORMAL  = spriteSheet.getCell(0, 2);
        Resources.SCORPION_FROZEN  = spriteSheet.getCell(0, 3);
        Resources.BULLET           = spriteSheet.getCell(1, 0);
        Resources.BLOOD            = spriteSheet.getCell(1, 1);
        Resources.SAND_BG          = spriteSheet.getCell(1, 2);

        // Bullet needs to be small than the normal textures
        float minU = (Resources.BULLET.getWidth()/2 - 8f) / Resources.BULLET.getWidth();
        float maxU = (Resources.BULLET.getWidth()/2 + 8f) / Resources.BULLET.getWidth();
        float minV = (Resources.BULLET.getHeight()/2 - 20.5f) / Resources.BULLET.getHeight();
        float maxV = (Resources.BULLET.getHeight()/2 + 20.5f) / Resources.BULLET.getHeight();

        Resources.BULLET = Resources.BULLET.getSubTexture(minU, minV, maxU, maxV);

        // Get the fonts from the loader
        Resources.HUD_FONT = loader.getFont(fontID);
        Resources.HUD_LARGE_FONT = loader.getFont(fontID2);

        // Get the sounds from the loader
        Resources.MUSIC = loader.getSound(musicID);
        Resources.SHOOT = loader.getSound(shootID);
        Resources.HURT  = loader.getSound(hurtID);

        // Play the music and loop it till the end of game
        Resources.MUSIC.setLooping(true);
        Resources.MUSIC.play();
    }

    public void update(float delta)
    {
        // Update the current GameState
        STATE.update(delta);
    }

    public void render(float delta, Batcher batcher)
    {
        // Render the current GameState
        STATE.render(delta, batcher);
    }

    public void resize()
    {
        Resources.CAMERA.initProjection(Display.getWidth(), Display.getHeight());
        Resources.HUD_CAMERA.initProjection(Display.getWidth(), Display.getHeight());
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
