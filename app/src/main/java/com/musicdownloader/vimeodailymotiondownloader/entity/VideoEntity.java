package com.musicdownloader.vimeodailymotiondownloader.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hanh Nguyen on 7/9/2017.
 */

public class VideoEntity implements Parcelable{

    private String videoUrl;
    private String thumbnailUrl;
    private String titleVideo;

    public VideoEntity(String url, String thumbnail, String title){
        videoUrl = url;
        thumbnailUrl = thumbnail;
        titleVideo = title;
    }

    public VideoEntity(Parcel source){
        this.videoUrl = source.readString();
        this.thumbnailUrl = source.readString();
        this.titleVideo = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoUrl);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.titleVideo);
    }

    public static final Creator<VideoEntity> CREATOR = new Creator<VideoEntity>() {
        @Override
        public VideoEntity createFromParcel(Parcel source) {
            return new VideoEntity(source);
        }

        @Override
        public VideoEntity[] newArray(int size) {
            return new VideoEntity[size];
        }
    };

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitleVideo() {
        return titleVideo;
    }
}
