package com.example.vastudio.fragments.Exhibition;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vastudio.Model.Exhibition;
import com.example.vastudio.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateExhibitionActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextArtist, editTextStartDate, editTextEndDate; // Add more EditText fields as needed
    private Button buttonCreateExhibition;
    private DatabaseReference exhibitionsRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_exhibition);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextArtist = findViewById(R.id.editTextArtist);
        editTextStartDate = findViewById(R.id.editTextDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);

        // Initialize other EditText fields
        buttonCreateExhibition = findViewById(R.id.buttonCreateExhibition);

        // Initialize Firebase
        exhibitionsRef = FirebaseDatabase.getInstance().getReference("exhibitions");

        buttonCreateExhibition.setOnClickListener(v -> createExhibition());
    }

    private void createExhibition() {
        String title = editTextTitle.getText().toString().trim();
        String artist = editTextArtist.getText().toString().trim();
        String startDate = editTextStartDate.getText().toString().trim();
        String endDate = editTextEndDate.getText().toString().trim();
        // Get other exhibition details from EditText fields

        if (title.isEmpty() || artist.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String exhibitionId = exhibitionsRef.push().getKey();
        Exhibition exhibition = new Exhibition(exhibitionId, title, artist, startDate, endDate);

        if (exhibitionId != null) {
            exhibitionsRef.child(exhibitionId).setValue(exhibition)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Exhibition created successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the activity after creating the exhibition
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to create exhibition", Toast.LENGTH_SHORT).show());
        }
    }
}
