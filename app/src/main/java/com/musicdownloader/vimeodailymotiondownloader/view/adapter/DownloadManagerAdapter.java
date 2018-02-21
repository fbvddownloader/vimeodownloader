package com.musicdownloader.vimeodailymotiondownloader.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.musicdownloader.vimeodailymotiondownloader.R;
import com.musicdownloader.vimeodailymotiondownloader.entity.DownloadMission;
import com.musicdownloader.vimeodailymotiondownloader.model.DownloadModel;
import com.musicdownloader.vimeodailymotiondownloader.view.CircularProgressIcon;
import com.musicdownloader.vimeodailymotiondownloader.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hanhanh.nguyen on 7/13/2017.
 */

public class DownloadManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DownloadMission> downloadMissionList;
    private OnItemClickListener onItemClickListener;
    private MainView mainView;

    @Inject
    public DownloadManagerAdapter(){
        this.downloadMissionList = new ArrayList<>();
    }

    public void setData(List<DownloadMission> list){
        downloadMissionList = list;
        notifyDataSetChanged();
    }

    public void setView(MainView view){
        this.mainView = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            final DownloadMission item = downloadMissionList.get(position);
            ((ViewHolder) holder).onBindView(position);
            ((ViewHolder)holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onClickListener(item);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (downloadMissionList == null) ? 0 : downloadMissionList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_download_card)
        CardView itemContainer;
        @BindView(R.id.item_download_image)
        ImageView backgroundImage;
        @BindView(R.id.item_download_stateIcon)
        CircularProgressIcon progressIcon;
        @BindView(R.id.item_download_title)
        TextView title;
        @BindView(R.id.item_delete_btn)
        ImageButton deleteItemBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindView(int position){
            mainView.loadBackgroundImage(backgroundImage, downloadMissionList.get(position).entity.thumbnail);
            switch (downloadMissionList.get(position).entity.result) {
                case DownloadModel.RESULT_DOWNLOADING:
                    progressIcon.forceSetProgressState();
                    title.setText(((int) (downloadMissionList.get(position).process)) + "%");
                    break;

                case DownloadModel.RESULT_SUCCEED:
                    progressIcon.forceSetResultState(R.drawable.ic_item_state_succeed);
                    //title.setText(downloadMissionList.get(position).entity.getNotificationTitle().toUpperCase());
                    break;

                case DownloadModel.RESULT_FAILED:
                    progressIcon.forceSetResultState(R.drawable.ic_item_state_error);
                    //title.setText(downloadMissionList.get(position).entity.getNotificationTitle().toUpperCase());
                    break;
            }
        }

        public void drawProcessStatus(DownloadMission mission, boolean switchState) {
            if (switchState) {
                progressIcon.setProgressState();
            }
            title.setText(((int) mission.process) + "%");
        }

        @OnClick(R.id.item_delete_btn)
        public void deleteItem(){
            mainView.deleteItem(downloadMissionList.get(getAdapterPosition()).entity.missionId);
            downloadMissionList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
        void onClickListener(DownloadMission item);
    }
}
