package com.musicdownloader.vimeodailymotiondownloader.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.musicdownloader.vimeodailymotiondownloader.VideoApplication;
import com.musicdownloader.vimeodailymotiondownloader.di.component.ActivityComponent;
import com.musicdownloader.vimeodailymotiondownloader.di.component.AppComponent;
import com.musicdownloader.vimeodailymotiondownloader.di.component.DaggerActivityComponent;
import com.musicdownloader.vimeodailymotiondownloader.di.module.ActivityModule;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initActivityComponent();
    }

    protected AppComponent getAppComponent(){
        return ((VideoApplication)getApplication()).getAppComponent();
    }

    public void initActivityComponent(){
        activityComponent = DaggerActivityComponent.builder()
                            .appComponent(getAppComponent())
                            .activityModule(new ActivityModule(this))
                            .build();
    }

    public ActivityComponent getActivityComponent(){
        return activityComponent;
    }
}
