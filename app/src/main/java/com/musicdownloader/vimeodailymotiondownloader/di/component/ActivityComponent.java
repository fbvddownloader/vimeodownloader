package com.musicdownloader.vimeodailymotiondownloader.di.component;

import com.musicdownloader.vimeodailymotiondownloader.di.PerActivity;
import com.musicdownloader.vimeodailymotiondownloader.di.module.ActivityModule;
import com.musicdownloader.vimeodailymotiondownloader.view.activity.MainActivity;
import com.musicdownloader.vimeodailymotiondownloader.view.activity.SplashActivity;
import com.musicdownloader.vimeodailymotiondownloader.view.activity.VimeoActivity;
import com.musicdownloader.vimeodailymotiondownloader.view.activity.VimeoVideoActivity;

import dagger.Component;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */
@PerActivity
@Component(
        dependencies = {AppComponent.class},
        modules = {ActivityModule.class}
)
public interface ActivityComponent {
    void inject(SplashActivity splashActivity);
    void inject(MainActivity mainActivity);
    void inject(VimeoActivity vimeoActivity);
    void inject(VimeoVideoActivity vimeoVideoActivity);
}
