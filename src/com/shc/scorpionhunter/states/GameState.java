package com.shc.scorpionhunter.states;

import com.shc.silenceengine.graphics.Batcher;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class GameState
{
    public abstract void update(float delta);

    public abstract void render(float delta, Batcher batcher);
}
