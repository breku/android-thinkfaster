package com.thinkfaster.activity;


import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.thinkfaster.manager.ResourcesManager;
import com.thinkfaster.manager.SceneManager;
import com.thinkfaster.service.GcmBroadCastReceiver;
import com.thinkfaster.service.MyGooglePlayService;
import com.thinkfaster.util.ConstantsUtil;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;

public class MyActivity extends BaseGameActivity {

    private static final String TAG = "MyActivity";
    private static final String SENDER_ID = "76038150616";
    private Camera camera;
    private AdView adView;
    private IntentFilter gcmFilter;
    private GcmBroadCastReceiver gcmBroadCastReceiver;
    private MyGooglePlayService myGooglePlayService =new MyGooglePlayService();

    private GoogleCloudMessaging gcm;
    private String registrationId;
    private Context context;

    @Override
    protected void onCreate(final Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        createGcmResources();
        registerDevice();

    }

    private void createGcmResources() {
        gcmBroadCastReceiver = new GcmBroadCastReceiver();
        gcmFilter = new IntentFilter();
        gcmFilter.addAction("GCM_RECEIVED_ACTION");
    }

    private void registerDevice() {
        Log.i(TAG,">> Registering device");
        boolean playServicesAvailable = myGooglePlayService.checkPlayServices(this);

        context = getApplicationContext();
        if (playServicesAvailable) {
            gcm = GoogleCloudMessaging.getInstance(this);

            registrationId = myGooglePlayService.getRegistrationId(context);
            Log.i(TAG,">> registration id from sharedPreferences:" + registrationId);

            if (registrationId.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.w(TAG, "No valid Google Play Services APK found.");

        }
        Log.i(TAG,"<< Registering device finished");

    }
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] objects) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    registrationId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + registrationId;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, registrationId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }
            private void storeRegistrationId(Context context, String regId) {
                final SharedPreferences prefs = myGooglePlayService.getGCMPreferences(context);
                int appVersion = myGooglePlayService.getAppVersion(context);
                Log.i(TAG, "Saving regId on app version " + appVersion);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PROPERTY_REG_ID, regId);
                editor.putInt(PROPERTY_APP_VERSION, appVersion);
                editor.commit();
            }
            private void sendRegistrationIdToBackend() {
                // Your implementation here.
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.i(TAG,"On post execute object=" + o);
                super.onPostExecute(o);
            }
        }.execute(null, null, null);

    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        registerReceiver(gcmBroadCastReceiver, gcmFilter);
        myGooglePlayService.checkPlayServices(this);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(gcmBroadCastReceiver);
        super.onPause();
    }

    @Override
    protected void onSetContentView() {
        Log.i(TAG, ">> Setting content view");

        FrameLayout frameLayout = new FrameLayout(this);

        this.mRenderSurfaceView = new RenderSurfaceView(this);
        mRenderSurfaceView.setRenderer(mEngine, this);

        try {
            adView = new AdView(this);
            adView.setTag("adView");
            adView.setAdUnitId(ConstantsUtil.MY_AD_UNIT_ID);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.refreshDrawableState();
            adView.setVisibility(AdView.VISIBLE);
            adView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM));

            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("2D91A564A65AF57C28A98B6EC9456D29")
                    .addTestDevice("7F8C8CB8DDF62CBD63E1AE7D4693C1F5")
                    .build();

            adView.loadAd(request);

            final FrameLayout.LayoutParams adViewLayoutParams =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);


            frameLayout.addView(this.mRenderSurfaceView);
            frameLayout.addView(adView, adViewLayoutParams);
        } catch (Exception e) {
            Log.e(TAG, "ADS ARE NOT WORKING\n,", e);
        }
        final FrameLayout.LayoutParams frameLayoutLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayoutLayoutParams.gravity = Gravity.BOTTOM;


        this.setContentView(frameLayout, frameLayoutLayoutParams);
        Log.i(TAG, "<< Setting content view finished");
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        Log.i(TAG, ">> Creating engine options");
        camera = new Camera(0, 0, ConstantsUtil.SCREEN_WIDTH, ConstantsUtil.SCREEN_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getRenderOptions().setDithering(true);
        Log.i(TAG, "<< Creating engine options finished");
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        Log.i(TAG, ">> Creating resources");
        ResourcesManager.prepareManager(getEngine(), this, camera, getVertexBufferObjectManager());
        pOnCreateResourcesCallback.onCreateResourcesFinished();
        Log.i(TAG, "<< Creating resources finished");
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        Log.i(TAG, ">> Creating scene");
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
        Log.i(TAG, "<< Creating scene finished");

    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        Log.i(TAG, ">> Populating scene");
        getEngine().registerUpdateHandler(new TimerHandler(ConstantsUtil.SPLASH_SCREEN_TIME, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                getEngine().unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMainMenuScene();
            }
        }));

        pOnPopulateSceneCallback.onPopulateSceneFinished();
        Log.i(TAG, "<< Populating scene finished");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false;
    }

    public AdView getAdView() {
        return adView;
    }
}
