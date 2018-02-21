package com.musicdownloader.vimeodailymotiondownloader.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.musicdownloader.vimeodailymotiondownloader.entity.DaoMaster;
import com.musicdownloader.vimeodailymotiondownloader.entity.DownloadMissionItem;

import java.util.List;

/**
 * Created by Hanh Nguyen on 7/13/2017.
 */

public class DatabaseModel {
    private static DatabaseModel instance;

    public static DatabaseModel getInstance(Context c){
        if(instance == null){
            synchronized (DatabaseModel.class){
                if(instance == null) {
                    instance = new DatabaseModel(c);
                }
            }
        }
        return instance;
    }

    private DaoMaster.DevOpenHelper openHelper;
    private static final String db_name = "downtube_video";

    private DatabaseModel(Context c){
        openHelper = new DaoMaster.DevOpenHelper(c, db_name, null);
    }

    public void writeDownloadItem(DownloadMissionItem item){
        DownloadMissionItem.insertDownloadItem(openHelper.getWritableDatabase(), item);
    }

    public DownloadMissionItem readDownloadItem(long missionId){
        return DownloadMissionItem.searchDownloadItem(openHelper.getReadableDatabase(), missionId);
    }

    public void updateDownloadEntity(DownloadMissionItem item){
        DownloadMissionItem.updateDownloadItem(openHelper.getReadableDatabase(), item);
    }

    public List<DownloadMissionItem> readDownloadItemList(int result) {
        return DownloadMissionItem.readDownloadItemList(openHelper.getReadableDatabase(), result);
    }

    public void removeDownloadItem(long missionId){
        DownloadMissionItem.deleteDownloadItem(openHelper.getWritableDatabase(), missionId);
    }
}
