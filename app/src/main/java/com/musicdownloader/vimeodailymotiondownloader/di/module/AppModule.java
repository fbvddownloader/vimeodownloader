package com.musicdownloader.vimeodailymotiondownloader.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.musicdownloader.vimeodailymotiondownloader.VideoApplication;
import com.musicdownloader.vimeodailymotiondownloader.model.DatabaseModel;
import com.musicdownloader.vimeodailymotiondownloader.model.UpdateModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */
@Module
public class AppModule {

    private static VideoApplication application;

    public AppModule(VideoApplication application){
        this.application = application;
    }

    @Singleton
    @Provides
    public Context provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofitBuilder(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(VideoApplication.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        Retrofit retrofit = builder.client(okHttpClient).build();
        return retrofit;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public DatabaseModel provideDatabaseModel(){
        return DatabaseModel.getInstance(application);
    }
}
