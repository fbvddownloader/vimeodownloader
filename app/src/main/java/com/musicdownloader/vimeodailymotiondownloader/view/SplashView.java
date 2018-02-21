package com.musicdownloader.vimeodailymotiondownloader.view;

import com.google.android.gms.ads.InterstitialAd;
import com.musicdownloader.vimeodailymotiondownloader.entity.UpdateInfoEntity;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */

public interface SplashView {
    void setUpdateInfo(UpdateInfoEntity entity);
    void showUpdateDialog(UpdateInfoEntity entity);
    void setInterstitialAd(InterstitialAd ads);
}
