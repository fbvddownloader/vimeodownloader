package com.musicdownloader.vimeodailymotiondownloader.presenter;

import android.widget.ImageView;

import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntity;
import com.musicdownloader.vimeodailymotiondownloader.model.DownloadModel;
import com.musicdownloader.vimeodailymotiondownloader.view.VimeoVideoView;

import javax.inject.Inject;

/**
 * Created by Hanh Nguyen on 7/10/2017.
 */

public class VimeoVideoPresenter {

    private VimeoVideoView vimeoVideoView;
    @Inject DownloadModel downloadModel;

    @Inject
    public VimeoVideoPresenter(){

    }

    public void setView(VimeoVideoView view){
        vimeoVideoView = view;
    }

    public void downloadVideo(VideoEntity item){
        downloadModel.downloadVideo(item);
    }

    public void loadPhoto(ImageView view, String url){
        downloadModel.loadPhoto(view, url);
    }
}
