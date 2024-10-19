package com.example.vastudio.Adapter;


import android.content.Context;
import android.content.Intent;
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
import com.example.vastudio.PaymentDialog;
import com.example.vastudio.R;
import com.example.vastudio.fragments.BidArtworkActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.PaintingViewHolder> {
    private List<Bid> paintingList;
    private Context context;

    public BidAdapter(List<Bid> paintingList, Context context) {
        this.paintingList = paintingList;
        this.context = context;
    }



    @NonNull
    @Override
    public PaintingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bid, parent, false);
        return new PaintingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaintingViewHolder holder, int position) {
        Bid painting = paintingList.get(position);
        // Bind painting data to views in the holder
        holder.bind(painting);
        holder.itemView.setOnClickListener(view -> {

            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("bids").child(painting.getBidId());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String status=snapshot.child("paymentStatus").getValue().toString();
                    if (status.equals("confirm")){
                        PaymentDialog paymentDialog=new PaymentDialog(context,String.valueOf(painting.getBidAmount()),painting.getBidId());
                        paymentDialog.show();
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Log.e("","Your status is pending");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//            Intent intent=new Intent(context,BidArtworkActivity.class);
//            intent.putExtra("postid",painting.getPostId());
//            intent.putExtra("artid",painting.getArtworkId());
//            intent.putExtra("publisheruid",painting.getPublisherid());
//            intent.putExtra("title",painting.getTitle());
//            intent.putExtra("amount",painting.getBidAmount());
//            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        Log.e("size", String.valueOf(paintingList.size()));
        return paintingList.size();
    }

    public static class PaintingViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPainting;
        private TextView textViewTitle;
        private TextView textViewArtist;

        private TextView textViewStat;

        public PaintingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPainting = itemView.findViewById(R.id.imageViewPainting);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewArtist = itemView.findViewById(R.id.textViewArtist);
            textViewStat = itemView.findViewById(R.id.textViewStat);
        }

        public void bind(Bid painting) {
            // Bind painting data to views
            textViewStat.setText(painting.getPaymentStatus());
            textViewTitle.setText(painting.getTitle());
            textViewArtist.setText(String.valueOf(painting.getBidAmount()));
            // Load image using Picasso/Glide or other image loading library
            Picasso.get().load(painting.getUrl()).into(imageViewPainting);

        }
    }

}

