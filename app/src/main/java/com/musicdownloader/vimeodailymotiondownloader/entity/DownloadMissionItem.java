package com.musicdownloader.vimeodailymotiondownloader.entity;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.musicdownloader.vimeodailymotiondownloader.model.DownloadModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;

/**
 * Created by Hanh Nguyen on 7/12/2017.
 */
@Entity
public class DownloadMissionItem {
    @Id
    public long missionId;

    public String url;
    public String name;
    public String thumbnail;

    @DownloadModel.DownloadResultRule
    public int result;

    public DownloadMissionItem(long missionId, String name, String url, String thumbnail){
        this.missionId = missionId;
        this.name = name;
        this.url = url;
        this.thumbnail = thumbnail;
    }

    @Generated(hash = 967999975)
    public DownloadMissionItem(long missionId, String url, String name, String thumbnail, int result) {
        this.missionId = missionId;
        this.url = url;
        this.name = name;
        this.thumbnail = thumbnail;
        this.result = result;
    }

    @Generated(hash = 498527211)
    public DownloadMissionItem() {
    }

    public static void insertDownloadItem(SQLiteDatabase database, @NonNull DownloadMissionItem item){
        DownloadMissionItem e = searchDownloadItem(database, item.missionId);
        if (e != null) {
            deleteDownloadItem(database, e.missionId);
        }
        new DaoMaster(database)
                .newSession()
                .getDownloadMissionItemDao()
                .insert(item);
    }

    @Nullable
    public static DownloadMissionItem searchDownloadItem(SQLiteDatabase database, long missionId) {
        List<DownloadMissionItem> itemList = new DaoMaster(database)
                .newSession()
                .getDownloadMissionItemDao()
                .queryBuilder()
                .where(DownloadMissionItemDao.Properties.MissionId.eq(missionId))
                .list();
        if (itemList != null && itemList.size() > 0) {
            return itemList.get(0);
        } else {
            return null;
        }
    }

    public static void deleteDownloadItem(SQLiteDatabase database, long missionId) {
        DownloadMissionItem entity = searchDownloadItem(database, missionId);
        if (entity != null) {
            new DaoMaster(database)
                    .newSession()
                    .getDownloadMissionItemDao()
                    .delete(entity);
        }
    }

    public static void updateDownloadItem(SQLiteDatabase database, DownloadMissionItem item){
        new DaoMaster(database)
                .newSession()
                .getDownloadMissionItemDao()
                .update(item);
    }

    public static List<DownloadMissionItem> readDownloadItemList(SQLiteDatabase database,
                                                                 @DownloadModel.DownloadResultRule int result) {
        return new DaoMaster(database)
                .newSession()
                .getDownloadMissionItemDao()
                .queryBuilder()
                .where(DownloadMissionItemDao.Properties.Result.eq(result))
                .list();
    }

    public long getMissionId() {
        return this.missionId;
    }

    public void setMissionId(long missionId) {
        this.missionId = missionId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
