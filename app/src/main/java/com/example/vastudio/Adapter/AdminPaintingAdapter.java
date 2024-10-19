package com.example.vastudio.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastudio.Model.Post;
import com.example.vastudio.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminPaintingAdapter extends RecyclerView.Adapter<AdminPaintingAdapter.PaintingViewHolder> {
    private List<Post> paintingList;
    private Context context;
    private List<Post> paintingListFull;
    public AdminPaintingAdapter(List<Post> paintingList, Context context) {
        this.paintingList = paintingList;
        this.context = context;
        this.paintingListFull = new ArrayList<>(paintingList);
    }


    public void restoreOriginal() {
        paintingList.clear();
        paintingList.addAll(paintingListFull);
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
            showApprovalDialog(painting);
        });
    }

    @Override
    public int getItemCount() {
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

    public void showApprovalDialog(Post post) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Approve Artwork")
                .setMessage("Are you sure you want to approve this artwork?")
                .setPositiveButton("Approve", (dialog, which) -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("approve");
                    String url = post.getImageUrl();
                    String id = post.getPostId();
                    String publisherUid = post.getPublisherUid();
                    String artist = post.getArtist();
                    String title = post.getTitle();
                    String des = post.getDsecription();
                    String price = post.getPrice();

                    Post post1 = new Post(url, id, publisherUid, artist, title, des, price);
                    databaseReference.child(id).setValue(post1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "approve artwork", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                    notifyDataSetChanged();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

