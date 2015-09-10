package com.thinkfaster.service.validator;

import com.thinkfaster.manager.ResourcesManager;
import com.thinkfaster.service.server.RegisterDeviceService;

import static com.thinkfaster.service.DeviceParametersService.isOnline;
import static com.thinkfaster.util.SceneUtils.gameToast;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by brekol on 07.05.15.
 */
public class DeviceValidator {

    private static final String NO_INTERNET_CONNECTION_MESSAGE = "You don't have internet connection";

    private static final String DEVICE_NOT_REGISTERED = "Your device is not registered, try again in a few seconds";

    private RegisterDeviceService registerDeviceService;


    public boolean validateRegistrationId() {
        registerDeviceService = getRegisterDeviceService();
        String registrationId = registerDeviceService.getRegistrationId();
        if (isBlank(registrationId)) {
            registerDeviceService.registerDevice();
            gameToast(DEVICE_NOT_REGISTERED);
            return false;
        }
        return true;
    }

    public boolean validateInternetConnection() {
        if (!isOnline()) {
            gameToast(NO_INTERNET_CONNECTION_MESSAGE);
            return false;
        }
        return true;
    }

    private RegisterDeviceService getRegisterDeviceService() {
        if (registerDeviceService != null) {
            return registerDeviceService;
        }
        return new RegisterDeviceService(ResourcesManager.getInstance().getActivity());


    }
}
