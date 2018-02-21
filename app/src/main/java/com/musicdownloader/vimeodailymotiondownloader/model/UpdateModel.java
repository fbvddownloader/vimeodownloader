package com.musicdownloader.vimeodailymotiondownloader.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musicdownloader.vimeodailymotiondownloader.entity.UpdateInfoEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Hanh Nguyen on 7/7/2017.
 */

public class UpdateModel {

    private Context context;
    private String updateInfoUrl = "";
    @Inject public OkHttpClient okHttpClient;

    @Inject
    public UpdateModel(Context context){
        this.context = context;
    }

    public Observable<UpdateInfoEntity> getUpdateInfo(){

        return Observable.fromCallable(new Callable<UpdateInfoEntity>() {
            @Override
            public UpdateInfoEntity call() throws Exception {
                Gson gson = new GsonBuilder().create();
                Request request = new Request.Builder()
                                .url(updateInfoUrl)
                                .build();
                Response response = okHttpClient.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String result, line = reader.readLine();
                result = line;
                while((line = reader.readLine()) != null){
                    result += line;
                }
                UpdateInfoEntity entity = gson.fromJson(result, UpdateInfoEntity.class);
                return entity;
            }
        })      .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
