package com.thinkfaster.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.thinkfaster.service.JsonRegisterService;
import com.thinkfaster.service.MyGooglePlayService;

import java.io.IOException;

import static com.thinkfaster.util.ContextConstants.SENDER_ID;
import static com.thinkfaster.util.SharedPreferencesKeys.APP_VERSION;
import static com.thinkfaster.util.SharedPreferencesKeys.REGISTRATION_ID;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by brekol on 05.05.15.
 */
public class RegisterTask extends AsyncTask<String, Void, String> {

    private final Context context;

    private MyGooglePlayService myGooglePlayService = new MyGooglePlayService();
    private GoogleCloudMessaging gcm;

    private JsonRegisterService jsonRegisterService = new JsonRegisterService();

    public RegisterTask(Context context) {
        this.context = context;
    }


    @Override
    protected String doInBackground(String... strings) {
        Log.i(TAG, ">> Registering device");
        String registrationId = getRegistrationId();
        sendRegistrationIdToServer(registrationId);
        storeRegistrationId(context, registrationId);
        return registrationId;
    }

    private String getRegistrationId() {
        if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(context);
        }
        try {
            String registrationId = gcm.register(SENDER_ID);
            Log.d(TAG, ">> Obtained registrationId=" + registrationId);
            return registrationId;
        } catch (IOException e) {
            Log.w(TAG, ">> Error ocured during registration", e);
        }
        return EMPTY;
    }

    private static final String TAG = "RegisterTask";


    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = myGooglePlayService.getGCMPreferences(context);
        int appVersion = myGooglePlayService.getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REGISTRATION_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }

    private void sendRegistrationIdToServer(String registrationId) {
        Log.i(TAG, ">> Sending registrationId to server");
        jsonRegisterService.sendRegistrationIdToServer(registrationId);
        Log.i(TAG, "<< Sending registrationId to server finished");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(TAG, "<< Task finished with result=" + result);
    }
}
