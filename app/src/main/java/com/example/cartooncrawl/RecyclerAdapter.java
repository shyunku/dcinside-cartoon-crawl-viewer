package com.example.cartooncrawl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{
    private ArrayList<PostInfo> infoList;
    private Context context;

    public RecyclerAdapter(ArrayList<PostInfo> infoList){
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        this.context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PostInfo postInfo = infoList.get(position);

        holder.time.setText(postInfo.getPostTime());
        holder.title.setText(postInfo.getPostTitle());
        holder.watched.setText(String.format("%d", postInfo.getWatchedNum()));
        holder.recommended.setText(String.format("%d", postInfo.getRecommendNum()));

        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContentsActivity.class);
            intent.putExtra("post_info", postInfo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView time;
        public TextView title;
        public TextView watched;
        public TextView recommended;

        public CardView card;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.post_time);
            title = itemView.findViewById(R.id.post_title);
            watched = itemView.findViewById(R.id.post_watched_num);
            recommended = itemView.findViewById(R.id.post_recommend_num);

            card = itemView.findViewById(R.id.card);
        }
    }
}
