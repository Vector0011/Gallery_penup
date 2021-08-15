package com.example.penup.adapters;

import android.content.Context;
import android.net.Uri;
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

public class GridMediaAdapter extends RecyclerView.Adapter<GridMediaAdapter.MediaViewHolder>{
    private Context mContext;
    private List<MediaModel> mListMedia;
    private RecycleViewClickItem clickItem;
    private ListMediaAdapter mainAdapter;

    public GridMediaAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData(List<MediaModel> mListMedia){
        this.mListMedia = mListMedia;
        notifyDataSetChanged();
    }
    public void setMainAdapter(ListMediaAdapter adapter){
        mainAdapter = adapter;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_media_item,parent,false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
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
        int index = mListMedia.get(position).getIndex();
        holder.selectedOverlay.setAlpha(mainAdapter.isSelected(index) ? 1 : 0.0f);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClick(view, index);
            }
        });
        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickItem.onLongClick(view, index);
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
