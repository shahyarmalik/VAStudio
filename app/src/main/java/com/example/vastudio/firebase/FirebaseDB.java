package com.example.vastudio.firebase;


import androidx.annotation.NonNull;

import com.example.vastudio.Model.Exhibition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDB {
    private DatabaseReference exhibitionsRef;

    public FirebaseDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        exhibitionsRef = database.getReference("exhibitions");
    }

    public void addExhibition(Exhibition exhibition) {
        String key = exhibitionsRef.push().getKey();
        if (key != null) {
            exhibition.setId(key);
            exhibitionsRef.child(key).setValue(exhibition);
        }
    }

    public void getAllExhibitions(final DataCallback<List<Exhibition>> callback) {
        exhibitionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Exhibition> exhibitions = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exhibition exhibition = snapshot.getValue(Exhibition.class);
                    if (exhibition != null) {
                        exhibitions.add(exhibition);
                    }
                }
                callback.onSuccess(exhibitions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    // Define a callback interface for asynchronous data retrieval
    public interface DataCallback<T> {
        void onSuccess(T data);

        void onError(String errorMessage);
    }
}

