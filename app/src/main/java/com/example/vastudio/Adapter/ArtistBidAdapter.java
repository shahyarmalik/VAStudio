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

import com.example.vastudio.Model.Bid;
import com.example.vastudio.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class ArtistBidAdapter extends RecyclerView.Adapter<ArtistBidAdapter.BidViewHolder> {
    private List<Bid> paintingList;
    final Context context;

    public ArtistBidAdapter(List<Bid> paintingList, Context context) {
        this.paintingList = paintingList;
        this.context = context;
    }


    @NonNull
    @Override
    public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_bid, parent, false);
        return new BidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {

        Bid painting = paintingList.get(position);

        // Bind painting data to views in the holder
        holder.bind(painting);

        holder.textViewTitle.setText(painting.getTitle());
        holder.textViewArtist.setText(String.valueOf(painting.getBidAmount()));
        holder.textStatus.setText(painting.getPaymentStatus());

        // Load image using Picasso/Glide or other image loading library
        Picasso.get().load(painting.getUrl()).into(holder.imageViewPainting);

        holder.itemView.setOnClickListener(view -> {
            showApprovalDialog(painting);
        });
    }

    @Override
    public int getItemCount() {
        Log.e("size", String.valueOf(paintingList.size()));
        return paintingList.size();
    }

    public static class BidViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPainting;
        private TextView textViewTitle;
        private TextView textViewArtist;
        private TextView textStatus;

        public BidViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPainting = itemView.findViewById(R.id.imageViewPainting);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewArtist = itemView.findViewById(R.id.textViewArtist);
            textStatus = itemView.findViewById(R.id.textStatus);
        }

        public void bind(Bid painting) {
            // Bind painting data to views

        }

    }

    public void showApprovalDialog(Bid bid) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Accept Bid");
        alertDialogBuilder.setMessage("Are you sure you want to accept this bid?");
        alertDialogBuilder.setPositiveButton("Accepted", (dialog, which) -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bids").child(bid.getBidId());
            //  Log.e("id",post.getBidId());
            // Update the status field of the bid to "confirm"
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("status", "confirm");
            databaseReference.updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {



                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


            notifyDataSetChanged();
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

