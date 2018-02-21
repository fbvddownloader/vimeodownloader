package com.musicdownloader.vimeodailymotiondownloader.entity;

/**
 * Created by hanhanh.nguyen on 7/12/2017.
 */

public class VideoEntityJson {
    /*
    "profile": 174,
    "width": 1280,
    "mime": "video/mp4",
    "fps": 23,
    "url": "https://gcs-vimeo.akamaized.net/exp=1499835633~acl=%2A%2F783671824.mp4%2A~hmac=13640357b1547ea3fe1023b00beff8ce4c83ecb8fb236e16fd395084dc5bcc4e/vimeo-prod-skyfire-std-us/01/4764/8/223823259/783671824.mp4",
    "cdn": "akamai_interconnect",
    "quality": "720p",
    "id": 783671824,
    "origin": "gcs",
    "height": 720
    */

    public String profile;
    public int width;
    public String mime;
    public int fps;
    public String url;
    public String cdn;
    public String quality;
    public long id;
    public String origin;
    public int height;
}
