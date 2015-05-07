package com.thinkfaster.activity;


import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdView;
import com.thinkfaster.manager.ResourcesManager;
import com.thinkfaster.manager.SceneManager;
import com.thinkfaster.service.AdvertService;
import com.thinkfaster.service.GcmBroadCastReceiver;
import com.thinkfaster.service.MyGooglePlayService;
import com.thinkfaster.service.RegisterDeviceService;
import com.thinkfaster.util.ContextConstants;
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

import static com.thinkfaster.util.ContextConstants.SPLASH_SCREEN_TIME;

public class MyActivity extends BaseGameActivity {

    private static final String TAG = "MyActivity";
    private Camera camera;
    private AdView adView;
    private IntentFilter gcmFilter;
    private GcmBroadCastReceiver gcmBroadCastReceiver;
    private MyGooglePlayService myGooglePlayService;
    private RegisterDeviceService registerDeviceService;
    private AdvertService advertService;

    @Override
    protected void onCreate(final Bundle pSavedInstanceState) {
        createServices();
        super.onCreate(pSavedInstanceState);
        createGcmResources();
        registerDevice();
    }

    private void createServices() {
        myGooglePlayService = new MyGooglePlayService();
        registerDeviceService = new RegisterDeviceService(this);
        advertService = new AdvertService(this);
    }

    private void createGcmResources() {
        gcmBroadCastReceiver = new GcmBroadCastReceiver();
        gcmFilter = new IntentFilter();
        gcmFilter.addAction("GCM_RECEIVED_ACTION");
    }

    private void registerDevice() {
        registerDeviceService.registerDevice();
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

    /**
     * called 1
     * @return
     */
    @Override
    public EngineOptions onCreateEngineOptions() {
        Log.i(TAG, ">> Creating engine options");
        camera = new Camera(0, 0, ContextConstants.SCREEN_WIDTH, ContextConstants.SCREEN_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);
        engineOptions.getRenderOptions().setDithering(true);
        Log.i(TAG, "<< Creating engine options finished");
        return engineOptions;
    }


    /**
     * called 2
     */
    @Override
    protected void onSetContentView() {
        Log.i(TAG, ">> Setting content view");

        FrameLayout frameLayout = new FrameLayout(this);

        this.mRenderSurfaceView = new RenderSurfaceView(this);
        mRenderSurfaceView.setRenderer(mEngine, this);

        try {
            adView = advertService.createAdView();
            final FrameLayout.LayoutParams adViewLayoutParams = advertService.getAdViewLayoutParams();
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

    /**
     * called 3, inside onCreateScene is called, it finishes after onCreateScene finishes
     * @param pOnCreateResourcesCallback
     * @throws IOException
     */
    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        Log.i(TAG, ">> Creating resources");
        ResourcesManager.prepareManager(getEngine(), this, camera, getVertexBufferObjectManager());
        SceneManager.prepareManager(this);
        pOnCreateResourcesCallback.onCreateResourcesFinished();
        Log.i(TAG, "<< Creating resources finished");
    }

    /**
     * called from onCreateResources
     * @param pOnCreateSceneCallback
     * @throws IOException
     */
    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        Log.i(TAG, ">> Creating scene");
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
        SceneManager.getInstance().initializeServices();
        Log.i(TAG, "<< Creating scene finished");

    }

    /**
     * called from onCreateScene
     * @param pScene
     * @param pOnPopulateSceneCallback
     * @throws IOException
     */
    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        Log.i(TAG, ">> Populating scene");
        getEngine().registerUpdateHandler(new TimerHandler(SPLASH_SCREEN_TIME, new ITimerCallback() {
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
