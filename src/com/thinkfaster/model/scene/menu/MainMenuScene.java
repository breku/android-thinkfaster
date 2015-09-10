package com.thinkfaster.model.scene.menu;

import com.thinkfaster.manager.ResourcesManager;
import com.thinkfaster.manager.SceneManager;
import com.thinkfaster.model.scene.BaseScene;
import com.thinkfaster.service.server.RegisterDeviceService;
import com.thinkfaster.service.validator.DeviceValidator;
import com.thinkfaster.util.ContextConstants;
import com.thinkfaster.util.SceneType;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class MainMenuScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {
    private final int NEW_GAME = 0;
    private final int MULTIPLAYER = 1;
    private final int ABOUT = 2;
    private final int EXIT = 3;
    private final int RECORDS = 4;
    private final int OPTIONS = 5;

    private MenuScene menuScene;
    private RegisterDeviceService registerDeviceService = new RegisterDeviceService(activity);
    private DeviceValidator deviceValidator = new DeviceValidator();

    @Override
    public void createScene(Object... objects) {
        createBackground();
        createButtons();
    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.MENU;
    }

    @Override
    public void disposeScene() {

    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case NEW_GAME:
                SceneManager.getInstance().loadGameTypeScene();
                break;
            case MULTIPLAYER:
                if (deviceValidator.validateInternetConnection() && deviceValidator.validateRegistrationId()) {
                    SceneManager.getInstance().loadWaitingRoomScene();
                }
                break;
            case ABOUT:
                SceneManager.getInstance().loadAboutScene();
                break;
            case EXIT:
                System.exit(0);
            case RECORDS:
                SceneManager.getInstance().loadHighScoreSceneFrom(SceneType.MENU, null, null, null);
                break;
            case OPTIONS:
                SceneManager.getInstance().loadOptionsScene();
                break;
            default:
                return false;
        }
        return false;
    }

    private void createBackground() {
        attachChild(new Sprite(ContextConstants.SCREEN_WIDTH / 2, ContextConstants.SCREEN_HEIGHT / 2, resourcesManager.getMenuBackgroundTextureRegion(), vertexBufferObjectManager));
    }

    private void createButtons() {
        menuScene = new MenuScene(camera);
        menuScene.setPosition(0, 0);

        final IMenuItem newGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(NEW_GAME, ResourcesManager.getInstance().getButtonNewGameTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem aboutItem = new ScaleMenuItemDecorator(new SpriteMenuItem(ABOUT, ResourcesManager.getInstance().getButtonAboutTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem multiplayerItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MULTIPLAYER, ResourcesManager.getInstance().getButtonMultiplayerTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem exitItem = new ScaleMenuItemDecorator(new SpriteMenuItem(EXIT, ResourcesManager.getInstance().getButtonExitTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem recordsItem = new ScaleMenuItemDecorator(new SpriteMenuItem(RECORDS, ResourcesManager.getInstance().getButtonHighScoreTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem optionsItem = new ScaleMenuItemDecorator(new SpriteMenuItem(OPTIONS, ResourcesManager.getInstance().getButtonOptionsTextureRegion(), vertexBufferObjectManager), 1.2f, 1);


        menuScene.addMenuItem(newGameItem);
        menuScene.addMenuItem(multiplayerItem);
        menuScene.addMenuItem(aboutItem);
        menuScene.addMenuItem(exitItem);
        menuScene.addMenuItem(recordsItem);
        menuScene.addMenuItem(optionsItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        newGameItem.setPosition(210, 397);
        multiplayerItem.setPosition(210, 327);
        recordsItem.setPosition(210, 257);
        aboutItem.setPosition(210, 187);
        exitItem.setPosition(210, 117);
        optionsItem.setPosition(210, 47);

        menuScene.setOnMenuItemClickListener(this);

        setChildScene(menuScene);

    }

}
