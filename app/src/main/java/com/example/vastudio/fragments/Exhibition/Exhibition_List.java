package com.example.vastudio.fragments.Exhibition;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastudio.Adapter.ExhibitionAdapter;
import com.example.vastudio.Model.Exhibition;
import com.example.vastudio.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Exhibition_List extends Fragment {
    private RecyclerView recyclerView;
    private ExhibitionAdapter adapter;
    private List<Exhibition> exhibitionList;
    private DatabaseReference exhibitionsRef;
    FloatingActionButton floatingActionButton;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exhibition__list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        floatingActionButton=rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new instance of the fragment you want to navigate to
              startActivity(new Intent(getContext(),CreateExhibitionActivity.class));
            }
        });
        exhibitionList = new ArrayList<>();

        adapter = new ExhibitionAdapter(exhibitionList,getContext());

        recyclerView.setAdapter(adapter);

        // Initialize Firebase
        exhibitionsRef = FirebaseDatabase.getInstance().getReference("exhibitions");

        loadExhibitions();

        return rootView;
    }



    private void loadExhibitions() {
        exhibitionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exhibitionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exhibition exhibition = snapshot.getValue(Exhibition.class);
                    if (exhibition != null) {
                        exhibitionList.add(exhibition);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load exhibitions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
