package com.shc.scorpionhunter.states;

import com.shc.silenceengine.graphics.Batcher;

/**
 * A GameState is a part of the game's life cycle.
 *
 * @author Sri Harsha Chilakapati
 */
public abstract class GameState
{
    /**
     * Used to do the logic of the game state. This method is called
     * almost 60 times per second since that is the target rate.
     *
     * @param delta This is the time taken for each frame in seconds.
     */
    public abstract void update(float delta);

    /**
     * This method is called whenever the display needs to be redrawn. Use
     * this method to render the contents of the game state to the screen.
     *
     * @param delta   This is the lag when rendering. 0 means no lag, and 1 means complete lag.
     * @param batcher This is the instance of the Batcher used to render graphics.
     */
    public abstract void render(float delta, Batcher batcher);
}
