package com.musicdownloader.vimeodailymotiondownloader.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicdownloader.vimeodailymotiondownloader.R;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntity;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntityJson;
import com.musicdownloader.vimeodailymotiondownloader.presenter.VimeoPresenter;
import com.musicdownloader.vimeodailymotiondownloader.view.VimeoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hanh Nguyen on 7/8/2017.
 */

public class VimeoActivity extends BaseActivity implements VimeoView {

    @BindView(R.id.vimeo_toolbar) Toolbar toolbar;
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.adView) FrameLayout adView;
    @BindView(R.id.btn_download) FloatingActionButton downloadBtn;

    @Inject SharedPreferences sharedPreferences;
    @Inject VimeoPresenter vimeoPresenter;
    private String vimeoUrl = "https://vimeo.com";
    private List<VideoEntityJson> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        vimeoPresenter.setView(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        initView();
    }

    private void initView(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VimeoActivity.super.onBackPressed();
            }
        });
        //initBannerAds();
        initWebView();
    }

    private void initBannerAds(){
        AdView view = new AdView(this);
        view.setAdSize(AdSize.SMART_BANNER);
        //view.setAdUnitId(sharedPreferences.getString(getString(R.string.banner_id_key), ""));
        adView.addView(view);
        AdRequest adRequest = new AdRequest.Builder().build();
        view.loadAd(adRequest);
    }

    private void initWebView(){
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(vimeoUrl);
    }

    @Override
    public void startVimeoVideoActivity(ArrayList<VideoEntity> list) {
        Intent intent = new Intent(this, VimeoVideoActivity.class);
        intent.putParcelableArrayListExtra("video_list", list);
        startActivity(intent);
    }

    @OnClick(R.id.btn_download)
    public void getVideoList(){
        if(webView.getUrl().substring(18) != null && !webView.getUrl().substring(18).isEmpty()) {
            String currentUrl = "https://player.vimeo.com/video/" + webView.getUrl().substring(18);
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            vimeoPresenter.getVideoList(currentUrl);
        } else {
            Toast.makeText(this, "Invalid url!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setVideoList(final List<VideoEntityJson> list) {
        final String[] qualityList = new String[list.size()];
        for(int i = 0; i < list.size(); i++) {
            VideoEntityJson item = list.get(i);
            String qualityItem = item.quality;
            qualityList[i] = qualityItem;
        }

        new MaterialDialog.Builder(this)
                .title(sharedPreferences.getString(getString(R.string.video_title_key), ""))
                .items(qualityList)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String selectedVd = Arrays.asList(qualityList).get(which);
                        for(VideoEntityJson item : list){
                            if(selectedVd.equals(item.quality)){
                                vimeoPresenter.downloadVideo(item.url, sharedPreferences.getString(getString(R.string.video_title_key), "") + ".mp4", sharedPreferences.getString(getString(R.string.video_thumbnail_key), ""));
                            }
                        }
                        return true;
                    }
                })
                .positiveText(R.string.download_action)
                .negativeText(R.string.cancel_action)
                .show();
    }

    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
