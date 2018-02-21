package com.musicdownloader.vimeodailymotiondownloader.presenter;

import android.content.Context;
import android.widget.ImageView;

import com.musicdownloader.vimeodailymotiondownloader.entity.DownloadMission;
import com.musicdownloader.vimeodailymotiondownloader.entity.DownloadMissionItem;
import com.musicdownloader.vimeodailymotiondownloader.model.DatabaseModel;
import com.musicdownloader.vimeodailymotiondownloader.model.DownloadModel;
import com.musicdownloader.vimeodailymotiondownloader.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Hanh Nguyen on 7/8/2017.
 */

public class MainPresenter {

    private MainView mainView;
    @Inject DownloadModel downloadModel;
    @Inject Context context;

    @Inject
    public MainPresenter(){

    }

    public void setView(MainView view){
        mainView = view;
    }

    public void loadBackgroundImage(ImageView imageView, String url){
        downloadModel.loadPhoto(imageView, url);
    }

    public void deleteMissionItem(long missionId){
        DownloadMissionItem misionItem = DatabaseModel.getInstance(context).readDownloadItem(missionId);
        if(misionItem != null && misionItem.result != DownloadModel.RESULT_SUCCEED) {
            DownloadModel.getInstance(context).downloadManager.remove(missionId);
        }
        DatabaseModel.getInstance(context).removeDownloadItem(missionId);
    }

    public List<DownloadMission> getDownloadMission(){
        List<DownloadMission> itemList = new ArrayList<>();
        List<DownloadMissionItem> entityList;
        entityList = DatabaseModel.getInstance(context).readDownloadItemList(DownloadModel.RESULT_FAILED);
        for (int i = 0; i < entityList.size(); i ++) {
            itemList.add(
                    new DownloadMission(
                            entityList.get(i)));
        }
        entityList = DatabaseModel.getInstance(context).readDownloadItemList(DownloadModel.RESULT_DOWNLOADING);
        for (int i = 0; i < entityList.size(); i ++) {
            itemList.add(
                    DownloadModel.getInstance(context)
                            .getDownloadMission(
                                    context,
                                    entityList.get(i).missionId));
        }
        entityList = DatabaseModel.getInstance(context).readDownloadItemList(DownloadModel.RESULT_SUCCEED);
        for (int i = 0; i < entityList.size(); i ++) {
            itemList.add(
                    new DownloadMission(
                            entityList.get(i)));
        }

        return itemList;
    }
}
