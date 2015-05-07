package com.thinkfaster.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.thinkfaster.manager.ResourcesManager;
import org.andengine.ui.activity.BaseGameActivity;

import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * Created by brekol on 07.05.15.
 */
public final class DeviceParametersService {

    private DeviceParametersService() {

    }

    public static boolean isOnline() {
        BaseGameActivity activity = ResourcesManager.getInstance().getActivity();
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            // make sure "HTC" is fully capitalized.
            return "HTC " + model;
        }
        return capitalize(manufacturer) + " " + model;
    }
}
