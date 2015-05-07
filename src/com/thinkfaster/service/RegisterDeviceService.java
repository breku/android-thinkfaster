package com.thinkfaster.service;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.thinkfaster.task.RegisterTask;

/**
 * Created by brekol on 05.05.15.
 */
public class RegisterDeviceService {

    private static final String TAG = "RegisterService";
    private final Activity activity;
    private Context context;
    private MyGooglePlayService myGooglePlayService = new MyGooglePlayService();

    public RegisterDeviceService(Activity activity) {
        this.activity = activity;
    }

    public void registerDevice() {
        Log.i(TAG, ">> Registering device");
        if (myGooglePlayService.checkPlayServices(activity)) {
            context = activity.getApplicationContext();
//            String registrationId = myGooglePlayService.getRegistrationIdFromSharedPreferences(context);
//            if (isBlank(registrationId)) {
            new RegisterTask(context).execute();
//            }

        } else {
            Log.w(TAG, "No valid Google Play Services APK found.");

        }
        Log.i(TAG, "<< Registering device finished");

    }


}
