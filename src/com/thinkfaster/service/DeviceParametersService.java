package com.thinkfaster.service;

import android.os.Build;

import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * Created by brekol on 07.05.15.
 */
public class DeviceParametersService {

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
