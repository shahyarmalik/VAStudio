package com.example.vastudio.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastudio.Model.Post;
import com.example.vastudio.R;
import com.example.vastudio.fragments.BidArtworkActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaintingAdapter extends RecyclerView.Adapter<PaintingAdapter.PaintingViewHolder> {
    private List<Post> paintingList;
    private Context context;

    public PaintingAdapter(List<Post> paintingList, Context context) {
        this.paintingList = paintingList;
        this.context = context;
    }

    public void updateList(List<Post> newList) {
        paintingList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaintingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_painting, parent, false);
        return new PaintingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaintingViewHolder holder, int position) {
        Post painting = paintingList.get(position);

        // Bind painting data to views in the holder
        holder.bind(painting);
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context,BidArtworkActivity.class);

            intent.putExtra("postid",painting.getPostId());
            intent.putExtra("artist",painting.getArtist());
            intent.putExtra("publisheruid",painting.getPublisherUid());
            intent.putExtra("imageurl",painting.getImageUrl());
            intent.putExtra("des",painting.getDsecription());
            intent.putExtra("title",painting.getTitle());

            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        Log.e("paintsize", String.valueOf(paintingList.size()));
        return paintingList.size();
    }

    public static class PaintingViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPainting;
        private TextView textViewTitle;
        private TextView textViewArtist;

        public PaintingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPainting = itemView.findViewById(R.id.imageViewPainting);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewArtist = itemView.findViewById(R.id.textViewArtist);
        }

        public void bind(Post painting) {
            // Bind painting data to views
            textViewTitle.setText(painting.getTitle());
            textViewArtist.setText(painting.getArtist());
            // Load image using Picasso/Glide or other image loading library
            Picasso.get().load(painting.getImageUrl()).into(imageViewPainting);

        }
    }
}

