package com.musicdownloader.vimeodailymotiondownloader.view;

import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntity;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntityJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanh Nguyen on 7/9/2017.
 */

public interface VimeoView {
    void startVimeoVideoActivity(ArrayList<VideoEntity> list);
    void setVideoList(List<VideoEntityJson> list);
}
