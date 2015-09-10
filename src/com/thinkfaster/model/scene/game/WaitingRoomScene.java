package com.thinkfaster.model.scene.game;

import com.thinkfaster.manager.ResourcesManager;
import com.thinkfaster.manager.SceneManager;
import com.thinkfaster.model.scene.BaseScene;
import com.thinkfaster.util.SceneType;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

import static com.thinkfaster.util.ContextConstants.SCREEN_HEIGHT;
import static com.thinkfaster.util.ContextConstants.SCREEN_WIDTH;
import static com.thinkfaster.util.SceneType.MULTIPLAYER_WAITING_ROOM;

/**
 * Created by brekol on 12.05.15.
 */
public class WaitingRoomScene extends BaseScene {


    @Override
    public void createScene(Object... objects) {
        createBackground();
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
        ResourcesManager.getInstance().unloadCommonTextures();
    }




    private void createBackground() {
        attachChild(new Sprite(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, resourcesManager.getBackgroundCommonsTextureRegion(), vertexBufferObjectManager));
    }
}
