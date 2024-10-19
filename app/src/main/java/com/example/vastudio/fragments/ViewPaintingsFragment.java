package com.example.vastudio.fragments;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.vastudio.Adapter.AdminPaintingAdapter;
import com.example.vastudio.DrawingCreation;
import com.example.vastudio.Model.Post;
import com.example.vastudio.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPaintingsFragment extends Fragment {
    private RecyclerView recyclerViewPaintings;
    private List<Post> paintingList;
    private AdminPaintingAdapter paintingAdapter;
    FloatingActionButton floatingActionButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_paintings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        floatingActionButton = view.findViewById(R.id.floataction);
//        floatingActionButton.setOnClickListener(view1 -> {
//            startActivity(new Intent(getContext(), DrawingCreation.class));
//        });
        SearchView searchView = view.findViewById(R.id.searchView);

        recyclerViewPaintings = view.findViewById(R.id.post_recyclerView);
        recyclerViewPaintings.setLayoutManager(new LinearLayoutManager(requireContext()));
        paintingList = new ArrayList<>();
        paintingAdapter = new AdminPaintingAdapter(paintingList, requireContext());
        recyclerViewPaintings.setAdapter(paintingAdapter);

        // Fetch paintings from Firebase and populate the list
        fetchPaintingsFromFirebase("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               fetchPaintingsFromFirebase(newText);
                return true;
            }
        });
    }

    private void restoreOriginalPaintings() {
        paintingAdapter.restoreOriginal();
    }

    private void fetchPaintingsFromFirebase() {
        DatabaseReference paintingsRef = FirebaseDatabase.getInstance().getReference("posts");
        paintingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                Toast.makeText(requireContext(), "Failed to fetch paintings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPaintingsFromFirebase(String searchQuery) {
        DatabaseReference paintingsRef = FirebaseDatabase.getInstance().getReference("posts");
        paintingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paintingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post painting = snapshot.getValue(Post.class);
                    if (painting != null && (searchQuery.isEmpty() || painting.getTitle().toLowerCase().contains(searchQuery.toLowerCase()))) {
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
}
