package com.shc.scorpionhunter.states;

import com.shc.scorpionhunter.Resources;
import com.shc.scorpionhunter.ScorpionHunter;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class IntroState extends GameState
{
    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        if (Keyboard.isClicked(Keyboard.KEY_SPACE))
            Game.setGameState(ScorpionHunter.PLAY_STATE);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        // Apply the HUD camera
        Resources.HUD_CAMERA.apply();

        TrueTypeFont font;

        // Position to draw the HUD_FONT
        float x, y;

        // Draw the logo using the large HUD_FONT in the center
        font = Resources.HUD_LARGE_FONT;

        String title = "ScorpionHunter";
        x = ScorpionHunter.CANVAS_WIDTH /2 - font.getWidth(title)/2;
        y = ScorpionHunter.CANVAS_HEIGHT /2 - font.getHeight()/2 - 20;

        font.drawString(batcher, title, x, y, Color.random());

        // Draw the message in the bottom
        font = Resources.HUD_FONT;

        String message = "Press SPACE to Play";
        x = ScorpionHunter.CANVAS_WIDTH /2 - font.getWidth(message)/2;
        y = ScorpionHunter.CANVAS_HEIGHT /2 - font.getHeight()/2 + 60;

        font.drawString(batcher, message, x, y, Color.WHITE);
    }
}
