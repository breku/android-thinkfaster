package com.thinkfaster.model.scene.game;

import com.thinkfaster.manager.ResourcesManager;
import com.thinkfaster.manager.SceneManager;
import com.thinkfaster.matcher.ClassTouchAreaMacher;
import com.thinkfaster.model.shape.GameButton;
import com.thinkfaster.model.shape.LifeBar;
import com.thinkfaster.model.shape.MathEquation;
import com.thinkfaster.model.shape.MathEquationText;
import com.thinkfaster.pool.MathEquationPool;
import com.thinkfaster.service.HighScoreService;
import com.thinkfaster.util.ContextConstants;
import com.thinkfaster.util.LevelDifficulty;
import com.thinkfaster.util.MathParameter;
import com.thinkfaster.util.SceneType;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import java.util.ArrayDeque;

import static com.thinkfaster.util.ContextConstants.INITIAL_POOL_SIZE;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class SinglePlayerGameScene extends AbstractGameScene {

    /**
     * @param objects objects[0] - levelDifficulty
     *                objects[1] - mathParameter
     *                objects[2] - multiplayer
     */
    public SinglePlayerGameScene(Object... objects) {
        super(objects);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SINGLE_PLAYER_GAME;
    }
}
