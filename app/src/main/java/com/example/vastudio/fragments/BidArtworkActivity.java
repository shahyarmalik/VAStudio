package com.example.vastudio.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vastudio.Model.Bid;
import com.example.vastudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class BidArtworkActivity extends AppCompatActivity {
    private TextView textViewArtworkTitle;
    private EditText editTextBidAmount;
    private Button buttonPlaceBid;
    private DatabaseReference bidsRef;

    private String postId,publisherid;

    private String artworkTitle,artWorkImageUrl;
    ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bid);

        textViewArtworkTitle = findViewById(R.id.textViewArtworkTitle);
        editTextBidAmount = findViewById(R.id.editTextBidAmount);
        imageView=findViewById(R.id.imageview);
        buttonPlaceBid = findViewById(R.id.buttonPlaceBid);
        bidsRef = FirebaseDatabase.getInstance().getReference("bids");

        Intent extras = getIntent();
        if (extras != null) {
            publisherid = extras.getStringExtra("publisheruid");
            postId = extras.getStringExtra("postid");
            artworkTitle = extras.getStringExtra("title");
            artWorkImageUrl = extras.getStringExtra("imageurl");
            textViewArtworkTitle.setText(artworkTitle);
        }
        Picasso.get().load(artWorkImageUrl).into(imageView);
        buttonPlaceBid.setOnClickListener(v -> placeBid());
    }

    private void placeBid() {
        String bidAmountStr = editTextBidAmount.getText().toString().trim();
        if (bidAmountStr.isEmpty()) {
            Toast.makeText(this, "Please enter a bid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double bidAmount = Double.parseDouble(bidAmountStr);
        String bidId = bidsRef.push().getKey();
//        HashMap hashMap=new HashMap();
//        hashMap.put("bidid",bidId);
//        hashMap.put("bidderid",FirebaseAuth.getInstance().getCurrentUser().getUid());
//        hashMap.put("postId",postId);
//        hashMap.put("artworkTitle",artworkTitle);
//        hashMap.put("publisherid",publisherid);
//        hashMap.put("artWorkImageUrl",artWorkImageUrl);
//        hashMap.put("bidAmount",bidAmount);
//        hashMap.put("status","Pending");

        // Assuming Bid is a Java class representing a bid
        Bid bid = new Bid(bidId,FirebaseAuth.getInstance().getCurrentUser().getUid(), postId,artworkTitle,publisherid,artWorkImageUrl,"Pending", bidAmount);
        // Push bid data to Firebase database

        if (bidId != null) {
            bidsRef.child(bidId).setValue(bid)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Bid placed successfully", Toast.LENGTH_SHORT).show();
                        // Clear EditText field or handle other UI actions
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to place bid", Toast.LENGTH_SHORT).show());
        }
    }
}
