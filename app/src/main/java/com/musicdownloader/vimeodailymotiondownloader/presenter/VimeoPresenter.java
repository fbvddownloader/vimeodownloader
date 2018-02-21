package com.musicdownloader.vimeodailymotiondownloader.presenter;

import android.content.Context;
import android.util.Base64;
import android.webkit.WebView;

import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntity;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntityJson;
import com.musicdownloader.vimeodailymotiondownloader.model.DownloadModel;
import com.musicdownloader.vimeodailymotiondownloader.view.VimeoView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Hanh Nguyen on 7/9/2017.
 */

public class VimeoPresenter {

    private VimeoView vimeoView;
    private ArrayList<VideoEntity> listVideo;
    private Context context;
    @Inject DownloadModel downloadModel;

    @Inject
    public VimeoPresenter(Context context){
        this.context = context;
        listVideo = new ArrayList<>();
    }

    public void setView(VimeoView view){
        vimeoView = view;
    }

    public void injectJS(WebView view, String scriptFile){
        listVideo.clear();
        InputStream input;
        try {
            input = context.getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();

            // String-ify the script byte-array using BASE64 encoding !!!
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addVideo(VideoEntity entity){
        listVideo.add(entity);
    }

    public void startVimeoVideoActivity(){
        vimeoView.startVimeoVideoActivity(listVideo);
    }

    public void getVideoList(String url){
        downloadModel.getVideoList(url).subscribe(new Observer<List<VideoEntityJson>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<VideoEntityJson> videoEntityJsons) {
                vimeoView.setVideoList(videoEntityJsons);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void downloadVideo(String url, String name, String thumbnail){
        downloadModel.downloadVideo(url, name, thumbnail);
    }
}
