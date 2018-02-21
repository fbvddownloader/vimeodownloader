package com.musicdownloader.vimeodailymotiondownloader.model;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicdownloader.vimeodailymotiondownloader.R;
import com.musicdownloader.vimeodailymotiondownloader.VideoApplication;
import com.musicdownloader.vimeodailymotiondownloader.entity.DownloadMission;
import com.musicdownloader.vimeodailymotiondownloader.entity.DownloadMissionItem;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntity;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntityJson;

import org.greenrobot.greendao.annotation.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Hanh Nguyen on 7/10/2017.
 */

public class DownloadModel {

    private String fieldResponse = "title,thumbnail_url,owner,views_total";

    public static final int RESULT_SUCCEED = 1;
    public static final int RESULT_FAILED = -1;
    public static final int RESULT_DOWNLOADING = 0;
    @IntDef({
            DownloadModel.RESULT_DOWNLOADING,
            DownloadModel.RESULT_SUCCEED,
            DownloadModel.RESULT_FAILED})
    public @interface DownloadResultRule {}

    public static String DOWNLOAD_PATH = "/Download/DownTubeVideos/";
    public DownloadManager downloadManager;
    private long downloadId;
    private Context context;

    private static DatabaseModel databaseModel;
    private static DownloadModel instance;
    @Inject SharedPreferences sharedPreferences;
    @Inject Retrofit retrofit;

    public static final DownloadModel getInstance(Context context){
        if(instance == null){
            synchronized (DownloadModel.class){
                if(instance == null)
                    instance = new DownloadModel(context);
            }
        }
        return instance;
    }

    @Inject
    public DownloadModel(Context context){
        this.context = context;
        downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        databaseModel = DatabaseModel.getInstance(context);
    }

    public void downloadVideo(String url, String name, String thumbnail){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(DOWNLOAD_PATH, name);
        downloadId = downloadManager.enqueue(request);

        DownloadMissionItem missionItem = new DownloadMissionItem(downloadId, name, url, thumbnail);
        missionItem.result = RESULT_DOWNLOADING;

        databaseModel.writeDownloadItem(missionItem);
    }

    public void downloadVideo(VideoEntity entity){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(entity.getVideoUrl()));
        request.allowScanningByMediaScanner();
        downloadId = downloadManager.enqueue(request);

        /*DownloadMissionItem missionItem = new DownloadMissionItem(item);
        missionItem.missionId = downloadId;
        missionItem.name = name;
        missionItem.result = RESULT_DOWNLOADING;

        databaseModel.writeDownloadItem(missionItem);
        listener.onStartDownload();*/
    }

    public static void downloadFinish(Context context, long missionId){
        DownloadMissionItem item = databaseModel.readDownloadItem(missionId);
        if (DownloadModel.getInstance(context).isMissionSuccess(missionId)) {
            if (item != null) {
                downloadSuccess(context, item);
                DownloadModel.getInstance(context)
                        .updateMissionResult(context, item.missionId, DownloadModel.RESULT_SUCCEED);
            }
        } else if (item != null) {
            downloadFailed(context, item);
            DownloadModel.getInstance(context)
                    .updateMissionResult(context, item.missionId, DownloadModel.RESULT_FAILED);
        }
    }

    public boolean isMissionSuccess(long missionId){
        Cursor cursor = getMissionCursor(missionId);
        if(cursor != null){
            int result = getDownloadResult(cursor);
            cursor.close();
            return  result == RESULT_SUCCEED;
        } else {
            return false;
        }
    }

    @Nullable
    private Cursor getMissionCursor(long missionId){
        Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(missionId));
        if(cursor == null){
            return null;
        } else if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            return cursor;
        } else {
            cursor.close();
            return null;
        }
    }

    private float getMissionProcess(@NotNull Cursor cursor) {
        long soFar = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        long total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        return (int) (100.0 * soFar / total);
    }

    private int getDownloadResult(Cursor cursor){
        switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_SUCCESSFUL:
                return RESULT_SUCCEED;

            case DownloadManager.STATUS_FAILED:
            case DownloadManager.STATUS_PAUSED:
                return RESULT_FAILED;

            default:
                return RESULT_DOWNLOADING;
        }
    }

    public static void downloadSuccess(Context context, DownloadMissionItem item){

    }

    public static void downloadFailed(Context context, DownloadMissionItem item){

    }

    public static void updateMissionResult(Context context, Long missionId, int result){
        DownloadMissionItem entity = DatabaseModel.getInstance(context).readDownloadItem(missionId);
        if (entity != null) {
            entity.result = result;
            DatabaseModel.getInstance(context).updateDownloadEntity(entity);
        }
    }

    @Nullable
    public DownloadMission getDownloadMission(Context context, long id) {
        DownloadMissionItem entity = DatabaseModel.getInstance(context).readDownloadItem(id);
        if (entity == null) {
            return null;
        } else {
            Cursor cursor = getMissionCursor(id);
            float process = 0;
            if (cursor != null) {
                entity.result = getDownloadResult(cursor);
                process = getMissionProcess(cursor);
                cursor.close();
            }
            return new DownloadMission(entity, process);
        }
    }

    public void loadPhoto(ImageView imageView, String thumbnailUrl){
        DrawableRequestBuilder<String> builder = Glide
                .with(context)
                .load(thumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                });

        builder.into(imageView);
    }


    public static void loadIcon(Context context, ImageView view,
                                int resId) {
        DrawableRequestBuilder<Integer> builder = Glide
                .with(context)
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);


        builder.into(view);
    }

    public static void releaseImageView(ImageView view) {
        Glide.clear(view);
    }

    public Observable<List<VideoEntityJson>> getVideoList(final String url){
        return Observable.fromCallable(new Callable<List<VideoEntityJson>>() {
            @Override
            public List<VideoEntityJson> call() throws Exception {
                Document doc;
                List<VideoEntityJson> videoList = new ArrayList<>();

                try{
                    doc = Jsoup.connect(url).get();
                    String bodyContent = doc.body().toString();

                    String regexUrl = "\"progressive\":(.*)\\}\\,\\\"lang\\\"";
                    String regexTitle = "\\\"title\\\"\\:\\\"(.*?)\\\"\\,";
                    String regexThumbnail = "\\\"base\\\"\\:\\\"(.*?)\\}";
                    Matcher matcherUrl = Pattern.compile(regexUrl).matcher(bodyContent);
                    Matcher matcherTitle = Pattern.compile(regexTitle).matcher(bodyContent);
                    Matcher matcherThumbnail = Pattern.compile(regexThumbnail).matcher(bodyContent);
                    if(matcherUrl.find() && matcherTitle.find() && matcherThumbnail.find()) {
                        Gson gson = new Gson();
                        Type videoEntityJsonType = new TypeToken<ArrayList<VideoEntityJson>>(){}.getType();
                        videoList = gson.fromJson(matcherUrl.group(1), videoEntityJsonType);
                        sharedPreferences.edit().putString(context.getString(R.string.video_title_key), matcherTitle.group(1)).apply();
                        sharedPreferences.edit().putString(context.getString(R.string.video_thumbnail_key), matcherThumbnail.group(1)).apply();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return videoList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * DM APIs
     */
    /*public Observable<SearchVideoEntity> searchVideos(final String query, final int page){
        return retrofit.create(SearchApi.class).searchVideos(query, page, VideoApplication.VIDEO_PER_PAGE, fieldResponse);
    }*/
}
