package com.musicdownloader.vimeodailymotiondownloader;

import android.app.Application;

import com.musicdownloader.vimeodailymotiondownloader.di.component.AppComponent;
import com.musicdownloader.vimeodailymotiondownloader.di.component.DaggerAppComponent;
import com.musicdownloader.vimeodailymotiondownloader.di.module.AppModule;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */

public class VideoApplication extends Application {

    public static final String BASE_URL = "https://vimeo.com";
    public static final int VIDEO_PER_PAGE = 10;
    private AppComponent appComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        initAppComponent();
    }

    private void initAppComponent(){
        appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
