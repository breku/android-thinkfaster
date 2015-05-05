package com.thinkfaster.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * User: Breku
 * Date: 2014-09-13
 */
public class GcmBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "GcmBroadCastReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, ">> Message received");
        Bundle bundle = intent.getExtras();
        for (String key : bundle.keySet()) {
            Log.i(TAG, ">> " + key + "=" + bundle.get(key));
        }

        Log.i(TAG, "<< Message received finished");
    }
}
