package com.thinkfaster.model.scene.game;

import com.thinkfaster.manager.SceneManager;
import com.thinkfaster.model.scene.BaseScene;
import com.thinkfaster.util.SceneType;

import static com.thinkfaster.util.SceneType.MULTIPLAYER_WAITING_ROOM;

/**
 * Created by brekol on 07.05.15.
 */
public class MultiplayerWaitingRoomScene extends BaseScene {
    @Override
    public void createScene(Object... objects) {

    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuScene(this);

    }

    @Override
    public SceneType getSceneType() {
        return MULTIPLAYER_WAITING_ROOM;
    }

    @Override
    public void disposeScene() {

    }
}
