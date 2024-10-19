package com.example.vastudio.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastudio.Adapter.BidAdapter;
import com.example.vastudio.Model.Bid;
import com.example.vastudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMyBidFragment extends Fragment {
    private RecyclerView recyclerViewPaintings;
    private List<Bid> paintingList=new ArrayList<>();
    private List<Bid> searchPaitingList=new ArrayList<>();

    private BidAdapter paintingAdapter;
    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;
    SearchView searchEditText;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_bid, container, false);





        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewPaintings = view.findViewById(R.id.post_recyclerView_bid);
        recyclerViewPaintings.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchEditText = view.findViewById(R.id.bid_editTextText);
        paintingAdapter = new BidAdapter(paintingList, requireContext());
        recyclerViewPaintings.setAdapter(paintingAdapter);


        // Get current user
        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchFirebase(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchFirebase("");


    }

    private void searchFirebase(String s) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            databaseRef = FirebaseDatabase.getInstance().getReference("bids");

            // Query posts by current user's publisherId
            Query query = databaseRef.orderByChild("bidderId").equalTo(currentUserId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    paintingList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Bid painting = snapshot1.getValue(Bid.class);
                        if (painting != null && (s.isEmpty() || painting.getTitle().toLowerCase().contains(s.toLowerCase()))) {
                            paintingList.add(painting);
                        }
                    }
                    paintingAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getContext(), "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }


    ValueEventListener valueEventListener =new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            paintingList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Bid painting = snapshot.getValue(Bid.class);
                if (painting != null ) {

                    paintingList.add(painting);
                }
            }
            paintingAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("ViewCurrentPublisher", "Error fetching paintings: " + databaseError.getMessage());
        }
    };

//    private ValueEventListener newvalueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            searchPaitingList.clear();
//            paintingList.clear();
//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                Post painting = snapshot.getValue(Post.class);
//                if (painting != null) {
//                    searchPaitingList.add(painting);
//                }
//            }
//            paintingAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//            Log.e("ViewCurrentPublisher", "Error fetching paintings: " + databaseError.getMessage());
//        }
//    };

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (databaseRef != null && valueEventListener != null) {
//            databaseRef.removeEventListener(valueEventListener);
//        }
//    }


}
