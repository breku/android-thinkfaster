package com.thinkfaster.service;

import android.app.Activity;
import android.view.Gravity;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.thinkfaster.util.ContextConstants;

/**
 * Created by brekol on 05.05.15.
 */
public class AdvertService {

    private final Activity activity;

    public AdvertService(Activity activity) {
        this.activity = activity;
    }

    public AdView createAdView() {
        AdView adView = new AdView(activity);
        adView.setTag("adView");
        adView.setAdUnitId(ContextConstants.MY_AD_UNIT_ID);
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
        return adView;
    }

    public FrameLayout.LayoutParams getAdViewLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
    }
}
