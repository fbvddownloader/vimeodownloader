package com.musicdownloader.vimeodailymotiondownloader;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.musicdownloader.vimeodailymotiondownloader.model.DownloadModel;

/**
 * Created by hanhanh.nguyen on 7/13/2017.
 */

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long missionId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        DownloadModel.downloadFinish(context, missionId);
    }
}