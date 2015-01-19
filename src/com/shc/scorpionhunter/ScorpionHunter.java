package com.shc.scorpionhunter;

import com.shc.scorpionhunter.states.GameOverState;
import com.shc.scorpionhunter.states.IntroState;
import com.shc.scorpionhunter.states.PauseState;
import com.shc.scorpionhunter.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.TrueTypeFont;

/**
 * This is the main Game class of the ScorpionHunter example game made using
 * SilenceEngine. This class takes care of resources, game states and the
 * game-play.
 *
 * @author Sri Harsha Chilakapati
 */
public class ScorpionHunter extends Game
{
    static
    {
        // You have to make it false when you are distributing the game.
        // Disabling development mode will speed up the game, because
        // OpenGL and OpenAL error checks which are costly will be disabled.
        development = true;
    }

    // The GameStates in this game.
    public static GameState CURRENT_STATE;
    public static GameState INTRO_STATE;
    public static GameState PLAY_STATE;
    public static GameState PAUSE_STATE;
    public static GameState GAMEOVER_STATE;

    // The SCORE and HEALTH of the player
    public static int SCORE;
    public static int HEALTH;

    // The WIDTH and HEIGHT of the CANVAS
    public static int CANVAS_WIDTH = 640;
    public static int CANVAS_HEIGHT = 480;

    /**
     * This method is called only once the Game starts.
     * This is used to handle initialization of the game.
     */
    public void init()
    {
        // Initialize the resources
        initResources();

        // Initialize the GameStates
        INTRO_STATE = new IntroState();
        PLAY_STATE = new PlayState();
        PAUSE_STATE = new PauseState();
        GAMEOVER_STATE = new GameOverState();

        // Initially show the INTRO state
        CURRENT_STATE = INTRO_STATE;
    }

    // This is a private method used to initialize resources
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

        // TEST
        System.out.println("BG size: " + Resources.SAND_BG.getWidth() + "x" + Resources.SAND_BG.getHeight());

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

    /**
     * Used to do the logic of the game. This method is called
     * almost 60 times per second since that is the target rate.
     *
     * @param delta This is the time taken for each frame in seconds.
     */
    public void update(float delta)
    {
        // Update the current GameState
        CURRENT_STATE.update(delta);
    }

    /**
     * This method is called whenever the display needs to be redrawn.
     * Use this method to render the contents of the game to the screen.
     *
     * @param delta   This is the lag when rendering. 0 means no lag, and 1 means complete lag.
     * @param batcher This is the instance of the Batcher used to render graphics.
     */
    public void render(float delta, Batcher batcher)
    {
        // Render the current GameState
        CURRENT_STATE.render(delta, batcher);
    }

    /**
     * This method is called whenever the user has resized the
     * game window. This also gets called when you switched screen
     * modes, and also maximized or minimized the game.
     */
    public void resize()
    {
        Resources.CAMERA.initProjection(Display.getWidth(), Display.getHeight());
    }

    /**
     * Called when the game is just about to exit. You have to use
     * this method to clean-up any of the resources you might be using.
     */
    public void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }

    /**
     * This is the entry point of this game.
     *
     * @param args The command line arguments if passed any.
     */
    public static void main(String[] args)
    {
        new ScorpionHunter().start();
    }
}
