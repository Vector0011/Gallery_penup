package com.example.penup.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.penup.R;
import com.example.penup.interfaces.RecycleViewClickItem;
import com.example.penup.models.MediaModel;
import com.example.penup.utils.DateTimeHelper;

import java.io.File;
import java.util.List;

public class FolderMediaAdapter extends SelectableAdapter<FolderMediaAdapter.MediaViewHolder> {
    private Context mContext;
    private List<MediaModel> mListMedia;
    private RecycleViewClickItem clickItem;

    public FolderMediaAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData(List<MediaModel> mListMedia){
        this.mListMedia = mListMedia;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FolderMediaAdapter.MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_folder_media_item,parent,false);
        return new FolderMediaAdapter.MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderMediaAdapter.MediaViewHolder holder, int position) {
        MediaModel media = mListMedia.get(position);
        if(media == null){
            return;
        }
        if(media.getType() == MediaModel.TYPE_VIDEO){
            holder.cvDuration.setVisibility(View.VISIBLE);
            holder.tvDuration.setText(DateTimeHelper.getInstance().getDuration(media.getDuration()));
        }else{
            holder.cvDuration.setVisibility(View.GONE);
        }
        int index = position;
        holder.selectedOverlay.setAlpha(isSelected(position) ? 1 : 0.0f);
        holder.selectedOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClick(view, index);
                Log.d("Vector","invisible");
            }
        });
        holder.selectedOverlay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickItem.onLongClick(view, index);
                Log.d("Vector","long invisible");
                return true;
            }
        });
        Glide.with(mContext).load(Uri.fromFile(new File(media.getPath()))).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        if(mListMedia!=null){
            return mListMedia.size();
        }
        return 0;
    }

    public void setCallBack(RecycleViewClickItem clickItem) {
        this.clickItem = clickItem;
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView tvDuration;
        View selectedOverlay;
        CardView cvDuration;
        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);
            cvDuration = itemView.findViewById(R.id.cv_duration);
        }
    }
}
