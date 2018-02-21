package com.musicdownloader.vimeodailymotiondownloader.di.module;

import com.musicdownloader.vimeodailymotiondownloader.di.PerActivity;
import com.musicdownloader.vimeodailymotiondownloader.view.activity.BaseActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */
@Module
public class ActivityModule {
    private final BaseActivity baseActivity;

    public ActivityModule(BaseActivity baseActivity){
        this.baseActivity = baseActivity;
    }

    @PerActivity
    @Provides
    public BaseActivity getBaseActivity(){
        return baseActivity;
    }
}
