package com.musicdownloader.vimeodailymotiondownloader.model;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */

public class AdsModel {

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private Context context;

    @Inject
    public AdsModel(Context context){
        this.context = context;
    }

    public Observable<InterstitialAd> loadAds(final String adsId){
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(adsId);
        adRequest = new AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_HASH")
                .build();

        mInterstitialAd.loadAd(adRequest);
        return Observable.just(mInterstitialAd);
    }
}
