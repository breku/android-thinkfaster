package com.thinkfaster.util;

import android.widget.Toast;
import com.thinkfaster.manager.ResourcesManager;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Created by brekol on 07.05.15.
 */
public final class SceneUtils {

    private SceneUtils() {

    }

    public static void gameToast(final String message) {
        final BaseGameActivity activity = ResourcesManager.getInstance().getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
