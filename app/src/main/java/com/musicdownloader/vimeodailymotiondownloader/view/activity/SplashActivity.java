package com.musicdownloader.vimeodailymotiondownloader.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.musicdownloader.vimeodailymotiondownloader.R;
import com.musicdownloader.vimeodailymotiondownloader.entity.UpdateInfoEntity;
import com.musicdownloader.vimeodailymotiondownloader.presenter.SplashPresenter;
import com.musicdownloader.vimeodailymotiondownloader.view.SplashView;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements SplashView {

    @Inject SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        splashPresenter.setView(this);
        loadUpdateInfo();
    }

    private void loadUpdateInfo(){
        splashPresenter.loadUpdateInfo();
    }

    @Override
    public void setUpdateInfo(UpdateInfoEntity entity) {

    }

    @Override
    public void showUpdateDialog(UpdateInfoEntity entity) {

    }

    @Override
    public void setInterstitialAd(final InterstitialAd ads) {
        ads.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                showMainActivity();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                showMainActivity();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                ads.show();
            }
        });
    }

    private void showMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
