package com.musicdownloader.vimeodailymotiondownloader.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicdownloader.vimeodailymotiondownloader.R;
import com.musicdownloader.vimeodailymotiondownloader.entity.VideoEntity;
import com.musicdownloader.vimeodailymotiondownloader.view.VimeoVideoView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hanh Nguyen on 7/10/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoEntity> videoList;
    private Context context;
    private VimeoVideoView videoView;
    private OnVideoItemClickedListener onItemClickedListener;

    @Inject
    public VideoAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.video_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            final VideoEntity item = videoList.get(position);
            ((ViewHolder)holder).onBindView(position);
            ((ViewHolder) holder).thumbnailView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(onItemClickedListener != null){
                        onItemClickedListener.onVideoItemClicked(item);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (videoList == null) ? 0 : videoList.size();
    }

    public void setOnItemClickedListener(OnVideoItemClickedListener listener){
        this.onItemClickedListener = listener;
    }

    public void setView(VimeoVideoView view){
        videoView = view;
    }

    public void setVideoList(List<VideoEntity> list){
        videoList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.thumbnail_view) ImageView thumbnailView;
        @BindView(R.id.video_title) TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindView(int position){
            videoView.loadPhoto(thumbnailView, videoList.get(position).getThumbnailUrl());
            titleView.setText(videoList.get(position).getTitleVideo());
        }
    }

    public interface OnVideoItemClickedListener{
        void onVideoItemClicked(VideoEntity item);
    }
}
