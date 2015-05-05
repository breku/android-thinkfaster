package com.thinkfaster.model.shape;

import com.thinkfaster.manager.ResourcesManager;
import com.thinkfaster.util.ContextConstants;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.adt.color.Color;

/**
 * User: Breku
 * Date: 20.10.13
 */
public class LifeBar extends Rectangle {

    private Color standardGreenColor = Color.WHITE;
    private Color goodGreenColor = new Color(0.5f, 1.0f, 0.0f);
    private Color redColor = new Color(1.0f, 0.0f, 0.0f);


    public LifeBar() {
        super(ContextConstants.SCREEN_WIDTH / 2, 460, 800, 30, ResourcesManager.getInstance().getVertexBufferObjectManager());
        setColor(standardGreenColor);
        setZIndex(1);

    }

    public void goodClick() {
        IEntityModifier colorModifier = new ColorModifier(ContextConstants.LIFE_BAR_COLOR_CHANGE_TIME, standardGreenColor, goodGreenColor) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                pItem.registerEntityModifier(new ColorModifier(ContextConstants.LIFE_BAR_COLOR_CHANGE_TIME, goodGreenColor, standardGreenColor));

            }
        };

        IEntityModifier moveModifier = new MoveByModifier(ContextConstants.LIFE_BAR_COLOR_CHANGE_TIME, ContextConstants.LIFE_BAR_AMOUNT_OF_GOOD_PIXES, 0);

        registerEntityModifier(moveModifier);
        registerEntityModifier(colorModifier);
    }

    public void wrongClick() {
        IEntityModifier colorModifier = new ColorModifier(ContextConstants.LIFE_BAR_COLOR_CHANGE_TIME, standardGreenColor, redColor) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                pItem.registerEntityModifier(new ColorModifier(ContextConstants.LIFE_BAR_COLOR_CHANGE_TIME, redColor, standardGreenColor));

            }
        };

        IEntityModifier moveModifier = new MoveByModifier(ContextConstants.LIFE_BAR_COLOR_CHANGE_TIME, -1 * ContextConstants.LIFE_BAR_AMOUNT_OF_WRONG_PIXES, 0);

        registerEntityModifier(moveModifier);
        registerEntityModifier(colorModifier);

    }

    public boolean isEnd() {
        if (getX() < -1 * ContextConstants.SCREEN_WIDTH / 2 + 5) {
            return true;
        }
        return false;
    }
}
