package com.example.vastudio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.FloatingWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastudio.Adapter.AdminPaintingAdapter;
import com.example.vastudio.Adapter.ArtistPaintingAdapter;
import com.example.vastudio.Adapter.PaintingAdapter;
import com.example.vastudio.Model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class ViewCurrentPublisherPaintingFragment extends Fragment {
    private RecyclerView recyclerViewPaintings;
    private List<Post> paintingList=new ArrayList<>();
    private List<Post> searchPaitingList=new ArrayList<>();

    private PaintingAdapter paintingAdapter;
    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;
    SearchView searchEditText;
    FloatingActionButton floatingActionButton;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curent_view_paintings, container, false);



        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton=view.findViewById(R.id.floataction);
        searchEditText = view.findViewById(R.id.searchView1);
        floatingActionButton.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(),DrawingCreation.class));

        });
        recyclerViewPaintings = view.findViewById(R.id.current_post_recyclerView);
        recyclerViewPaintings.setLayoutManager(new LinearLayoutManager(requireContext()));
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        paintingAdapter = new PaintingAdapter(paintingList, requireContext());
        recyclerViewPaintings.setAdapter(paintingAdapter);
        // Get current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseRef = FirebaseDatabase.getInstance().getReference("posts");

        // Query posts by current user's publisherId
        Query query = databaseRef.orderByChild("publisherUid").equalTo(currentUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.e("snapshot",snapshot.toString());
                    paintingList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Post painting = snapshot1.getValue(Post.class);
                        if (painting != null ) {
                            paintingList.add(painting);
                        }


                    }
                    paintingAdapter.notifyDataSetChanged();
                }
                else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
             //   searchFirebase(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    //    searchFirebase("");


    }

    private void searchFirebase(String s) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Log.e("currentid",currentUserId);
            databaseRef = FirebaseDatabase.getInstance().getReference("posts");

            // Query posts by current user's publisherId
            Query query = databaseRef.orderByChild("publisherUid").equalTo(currentUserId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Log.e("snapshot",snapshot.toString());
                        paintingList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Post painting = snapshot.getValue(Post.class);
                            if (painting != null && (s.isEmpty() || painting.getTitle().toLowerCase().contains(s.toLowerCase()))) {
                                paintingList.add(painting);
                            }


                        }
                        paintingAdapter.notifyDataSetChanged();
                    }
                    else{

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getContext(), "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }


    private ValueEventListener valueEventListener =new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            paintingList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Post painting = snapshot.getValue(Post.class);
//                if (painting != null && (s.isEmpty() || painting.getTitle().toLowerCase().contains(searchQuery.toLowerCase()))) {
//                    paintingList.add(painting);
//                }
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
