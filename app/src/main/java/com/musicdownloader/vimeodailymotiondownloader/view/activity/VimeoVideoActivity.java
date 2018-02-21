package com.musicdownloader.vimeodailymotiondownloader.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.musicdownloader.vimeodailymotiondownloader.R;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntity;
import com.musicdownloader.vimeodailymotiondownloader.presenter.VimeoVideoPresenter;
import com.musicdownloader.vimeodailymotiondownloader.view.VimeoVideoView;
import com.musicdownloader.vimeodailymotiondownloader.view.adapter.VideoAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hanh Nguyen on 7/10/2017.
 */

public class VimeoVideoActivity extends BaseActivity implements VimeoVideoView{

    @BindView(R.id.recyclerView_videos) RecyclerView recyclerView;
    @BindView(R.id.videos_toolbar) Toolbar toolbar;

    @Inject VimeoVideoPresenter vimeoVideoPresenter;
    @Inject VideoAdapter videoAdapter;
    private List<VideoEntity> videoList;

    private VideoAdapter.OnVideoItemClickedListener itemClickedListener = new VideoAdapter.OnVideoItemClickedListener() {
        @Override
        public void onVideoItemClicked(VideoEntity item) {
            vimeoVideoPresenter.downloadVideo(item);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo_video);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        vimeoVideoPresenter.setView(this);
        initView();
    }

    private void initView(){
        setSupportActionBar(toolbar);
        videoList = getIntent().getParcelableArrayListExtra("video_list");
        videoAdapter.setVideoList(videoList);
        videoAdapter.setView(this);
        videoAdapter.setOnItemClickedListener(itemClickedListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(videoAdapter);
    }

    @Override
    public void loadPhoto(ImageView view, String url) {
        vimeoVideoPresenter.loadPhoto(view, url);
    }

}
