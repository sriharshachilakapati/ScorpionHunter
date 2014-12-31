package com.shc.scorpionhunter;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    // Textures used in the game
    public static Texture SHOOTER_STANDING;
    public static Texture SHOOTER_SHOOTING;
    public static Texture SCORPION_NORMAL;
    public static Texture SCORPION_FROZEN;
    public static Texture BULLET;
    public static Texture BLOOD;
    public static Texture SAND_BG;

    // Sounds used in the game
    public static Sound MUSIC;
    public static Sound SHOOT;
    public static Sound HURT;

    // Fonts used in the game
    public static TrueTypeFont HUD_FONT;
    public static TrueTypeFont HUD_LARGE_FONT;

    // Cameras used in the game
    public static OrthoCam CAMERA;
    public static OrthoCam HUD_CAMERA;
}
