package com.musicdownloader.vimeodailymotiondownloader.presenter;

import com.google.android.gms.ads.InterstitialAd;
import com.musicdownloader.vimeodailymotiondownloader.entity.UpdateInfoEntity;
import com.musicdownloader.vimeodailymotiondownloader.model.AdsModel;
import com.musicdownloader.vimeodailymotiondownloader.model.UpdateModel;
import com.musicdownloader.vimeodailymotiondownloader.view.SplashView;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */

public class SplashPresenter {

    private SplashView splashView;
    @Inject UpdateModel updateModel;
    @Inject AdsModel adsModel;

    @Inject
    public SplashPresenter(){

    }

    public void setView(SplashView view){
        splashView = view;
    }

    public void loadUpdateInfo(){
        updateModel.getUpdateInfo().subscribe(new Observer<UpdateInfoEntity>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull UpdateInfoEntity updateInfoEntity) {
                splashView.setUpdateInfo(updateInfoEntity);
                if(updateInfoEntity.update == true) {
                    splashView.showUpdateDialog(updateInfoEntity);
                }
                loadInterstitialAds(updateInfoEntity.interstitial_ads);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void loadInterstitialAds(String adsId){
        adsModel.loadAds(adsId).subscribe(new Observer<InterstitialAd>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull InterstitialAd interstitialAd) {
                splashView.setInterstitialAd(interstitialAd);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
