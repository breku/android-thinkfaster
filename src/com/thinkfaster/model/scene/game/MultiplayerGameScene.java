package com.thinkfaster.model.scene.game;

import com.thinkfaster.manager.SceneManager;
import com.thinkfaster.util.SceneType;

import static com.thinkfaster.util.SceneType.MULTIPLAYER_GAME;

/**
 * Created by brekol on 07.05.15.
 */
public class MultiplayerGameScene extends AbstractGameScene {


    @Override
    public void createScene(Object... objects) {

    }



    @Override
    public SceneType getSceneType() {
        return MULTIPLAYER_GAME;
    }

    @Override
    public void disposeScene() {

    }
}
