package com.musicdownloader.vimeodailymotiondownloader.view;

import android.widget.ImageView;

/**
 * Created by Hanh Nguyen on 7/8/2017.
 */

public interface MainView {
    void loadBackgroundImage(ImageView view, String url);
    void deleteItem(long missionId);
}
