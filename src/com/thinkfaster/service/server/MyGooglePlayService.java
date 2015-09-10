package com.thinkfaster.service.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.thinkfaster.activity.MyActivity;

import static com.thinkfaster.util.SharedPreferencesKeys.APP_VERSION;
import static com.thinkfaster.util.SharedPreferencesKeys.REGISTRATION_ID;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by brekol on 05.05.15.
 */
public class MyGooglePlayService {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MyGooglePlayService";

    public boolean checkPlayServices(final Activity activity) {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                                PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    }
                });

            } else {
                Log.w(TAG, "<< This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public String getRegistrationIdFromSharedPreferences(Context context) {
        Log.i(TAG, ">> get registration id from sharedPreferences called");
        final SharedPreferences sharedPreferences = getGCMPreferences(context);
        String registrationId = sharedPreferences.getString(REGISTRATION_ID, EMPTY);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "<< Registration not found.");
            return EMPTY;
        }
        if (appVersionChanged(sharedPreferences, context)) {
            Log.i(TAG, "<< App version changed.");
            return EMPTY;
        }
        Log.i(TAG, "<< get registration id from sharedPreferences finished with registrationId=" + registrationId);
        return registrationId;
    }

    public SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(MyActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: ", e);
        }
    }

    private boolean appVersionChanged(SharedPreferences sharedPreferences, Context context) {
        int registeredVersion = sharedPreferences.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        return registeredVersion != currentVersion;
    }
}
