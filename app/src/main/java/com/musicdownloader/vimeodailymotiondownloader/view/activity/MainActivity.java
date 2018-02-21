package com.musicdownloader.vimeodailymotiondownloader.view.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.musicdownloader.vimeodailymotiondownloader.R;
import com.musicdownloader.vimeodailymotiondownloader.VideoApplication;
import com.musicdownloader.vimeodailymotiondownloader.entity.DownloadMission;
import com.musicdownloader.vimeodailymotiondownloader.model.DownloadModel;
import com.musicdownloader.vimeodailymotiondownloader.presenter.MainPresenter;
import com.musicdownloader.vimeodailymotiondownloader.view.MainView;
import com.musicdownloader.vimeodailymotiondownloader.view.adapter.DownloadManagerAdapter;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hanh Nguyen on 7/8/2017.
 */

public class MainActivity extends BaseActivity implements MainView{

    @Inject MainPresenter mainPresenter;
    @Inject DownloadManagerAdapter adapter;

    @BindView(R.id.recyclerView_download_manager) RecyclerView recyclerView;
    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.vimeo_fab) FloatingActionButton vimeoFab;
    @BindView(R.id.dm_fab) FloatingActionButton dmFab;

    private List<DownloadMission> downloadMissionList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getActivityComponent().inject(this);
        mainPresenter.setView(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        initView();
        initData();
        initObservable(false);
    }

    private void initObservable(final boolean running){

        Observable.fromCallable(new Callable<DownloadMission>() {
            @Override
            public DownloadMission call() throws Exception {
                for (int i = 0; i < downloadMissionList.size(); i++) {
                    if (downloadMissionList.get(i).entity.result == DownloadModel.RESULT_DOWNLOADING) {
                        DownloadMission mission = DownloadModel.getInstance(MainActivity.this)
                                .getDownloadMission(
                                        MainActivity.this,
                                        downloadMissionList.get(i).entity.missionId);
                        if (mission != null
                                && (mission.entity.result == DownloadModel.RESULT_DOWNLOADING
                                || mission.entity.result != downloadMissionList.get(i).entity.result)) {
                            return mission;
                        }

                    }
                }
                return null;
            }
        }).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@io.reactivex.annotations.NonNull Observable<Object> objectObservable) throws Exception {
                return objectObservable.delay(100, TimeUnit.MILLISECONDS);
            }
        })
                .takeUntil(new Predicate<DownloadMission>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull DownloadMission downloadMission) throws Exception {
                        return running;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadMission>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull DownloadMission downloadMission) {
                        DownloadMission newMission = downloadMission;
                        // try to find the mission's position.
                        int index = -1;
                        for (int i = 0; i < adapter.getItemCount(); i++) {
                            if (downloadMissionList.get(i).entity.missionId == newMission.entity.missionId) {
                                index = i;
                                break;
                            }
                        }
                        if (index == -1) {
                            // cannot find the mission's position.
                            return;
                        }
                        DownloadMission oldMission = downloadMissionList.get(index);
                        switch (newMission.entity.result) {
                            case DownloadModel.RESULT_DOWNLOADING:
                                if (oldMission.entity.result != DownloadModel.RESULT_DOWNLOADING) {
                                    DownloadModel.getInstance(MainActivity.this)
                                            .updateMissionResult(
                                                    MainActivity.this,
                                                    newMission.entity.missionId,
                                                    DownloadModel.RESULT_DOWNLOADING);
                                    drawRecyclerItemProcess(index, newMission, true);
                                } else if (oldMission.process != newMission.process) {
                                    drawRecyclerItemProcess(index, newMission, false);
                                }
                                break;

                            case DownloadModel.RESULT_SUCCEED:
                                if (oldMission.entity.result != DownloadModel.RESULT_SUCCEED) {
                                    DownloadModel.getInstance(MainActivity.this)
                                            .updateMissionResult(
                                                    MainActivity.this,
                                                    newMission.entity.missionId,
                                                    DownloadModel.RESULT_SUCCEED);
                                    drawRecyclerItemSucceed(index, newMission);
                                }
                                break;

                            case DownloadModel.RESULT_FAILED:
                                if (oldMission.entity.result != DownloadModel.RESULT_FAILED) {
                                    DownloadModel.getInstance(MainActivity.this)
                                            .updateMissionResult(
                                                    MainActivity.this,
                                                    newMission.entity.missionId,
                                                    DownloadModel.RESULT_FAILED);
                                    drawRecyclerItemFailed(index, newMission);
                                }
                                break;
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initView(){
        recyclerView.setAdapter(adapter);
        adapter.setView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(OnDownloadItemClickListener);
    }

    private void initData(){
        downloadMissionList = mainPresenter.getDownloadMission();
        adapter.setData(downloadMissionList);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions(){
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        for (int i = 0; i < permission.length; i ++) {
            switch (permission[i]) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    if (grantResult[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void loadBackgroundImage(ImageView view, String url) {
        mainPresenter.loadBackgroundImage(view, url);
    }

    @Override
    public void deleteItem(long missionId) {
        mainPresenter.deleteMissionItem(missionId);
    }

    @OnClick(R.id.vimeo_fab)
    public void startVimeoActivity(){
        startActivity(new Intent(this, VimeoActivity.class));
    }

    @OnClick(R.id.dm_fab)
    public void startDailyMotionActivit(){
        //startActivity(new Intent(this, DailyMotionActivity.class));
        Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
    }

    DownloadManagerAdapter.OnItemClickListener OnDownloadItemClickListener = new DownloadManagerAdapter.OnItemClickListener(){
        @Override
        public void onClickListener(DownloadMission item) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri uri = filePathToUri(
                    MainActivity.this,
                    Environment.getExternalStorageDirectory()
                            + DownloadModel.DOWNLOAD_PATH
                            + item.entity.name);
            intent.setDataAndType(uri, "video/mp4");

            MainActivity.this.startActivity(
                    Intent.createChooser(
                            intent,
                            getString(R.string.play_video)));
        }
    };

    public static Uri filePathToUri(Context context, @NonNull String filePath) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {filePath.substring(filePath.lastIndexOf("/") + 1)},
                null);

        Uri uri = Uri.parse("file://" + filePath);
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                if (index > -1) {
                    uri = ContentUris.withAppendedId(mediaUri, cursor.getLong(index));
                }
            }
            cursor.close();
        }
        return uri;
    }

    /**
     * Make item view show downloading progress and percent.
     *
     * @param position    Adapter position for item.
     * @param mission     A {@link DownloadMission} object which saved information of downloading task.
     * @param switchState If set true, that means the item view will switch from another state to
     *                    the downloading state.
     * */
    private void drawRecyclerItemProcess(int position, DownloadMission mission, boolean switchState) {
        downloadMissionList.set(position, mission);

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        int lastPosition = layoutManager.findLastVisibleItemPosition();
        if (firstPosition <= position && position <= lastPosition) {
            // we doesn't need to refresh a item view that is not displayed.
            DownloadManagerAdapter.ViewHolder holder
                    = (DownloadManagerAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.drawProcessStatus(mission, switchState);
            }
        }
    }

    /**
     * Make the item view to show the information that means "Download successful".
     *
     * @param position Adapter position for item.
     * @param mission  A {@link DownloadMission} object which saved information of downloading task.
     * */
    private void drawRecyclerItemSucceed(int position, DownloadMission mission) {
        downloadMissionList.remove(position);
        adapter.notifyItemRemoved(position);

        for (int i = downloadMissionList.size() - 1; i >= 0; i --) {
            if (downloadMissionList.get(i).entity.result != DownloadModel.RESULT_SUCCEED) {
                downloadMissionList.add(i + 1, mission);
                adapter.notifyItemInserted(i + 1);
                return;
            }
        }
        downloadMissionList.add(0, mission);
        adapter.notifyItemInserted(0);
    }

    /**
     * Make the item view to show the information that means "Download failed".
     *
     * @param position Adapter position for item.
     * @param mission  A {@link DownloadMission} object which saved information of downloading task.
     * */
    private void drawRecyclerItemFailed(int position, DownloadMission mission) {
        // remove the old item and add a new item on the first position of list.

        downloadMissionList.remove(position);
        adapter.notifyItemRemoved(position);

        downloadMissionList.add(0, mission);
        adapter.notifyItemInserted(0);
    }
}
