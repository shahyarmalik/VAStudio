package com.example.vastudio;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.vastudio.Adapter.PaintingAdapter;
import com.example.vastudio.Model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewVisitorPaintingFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.visitor_view_paintings, container, false);

        searchEditText = view.findViewById(R.id.editTextText);



    return view;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewPaintings = view.findViewById(R.id.post_recyclerView);
        recyclerViewPaintings.setLayoutManager(new LinearLayoutManager(requireContext()));

        paintingAdapter = new PaintingAdapter(paintingList, requireContext());
        recyclerViewPaintings.setAdapter(paintingAdapter);


        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                fetchPaintingsFromSearch(newText);
                return true;
            }
        });
        fetchPaintingsFromSearch("");


    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            searchPaitingList.clear();
            paintingList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Post painting = snapshot.getValue(Post.class);
                if (painting != null) {
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

    private ValueEventListener newvalueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
       //     searchPaitingList.clear();
            paintingList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Post painting = snapshot.getValue(Post.class);
                if (painting != null) {
                    searchPaitingList.add(painting);
                }
            }
            paintingAdapter = new PaintingAdapter(searchPaitingList, requireContext());
            recyclerViewPaintings.setAdapter(paintingAdapter);
            paintingAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("ViewCurrentPublisher", "Error fetching paintings: " + databaseError.getMessage());
        }
    };


    private void fetchPaintingsFromSearch(String newText) {
        DatabaseReference paintingsRef = FirebaseDatabase.getInstance().getReference("approve");
        paintingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paintingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post painting = snapshot.getValue(Post.class);
                    if (painting != null && (newText.isEmpty() || painting.getTitle().toLowerCase().contains(newText.toLowerCase()))) {
                        paintingList.add(painting);
                    }
                }
                paintingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to fetch paintings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseRef != null && valueEventListener != null) {
            databaseRef.removeEventListener(valueEventListener);
        }
    }


}
