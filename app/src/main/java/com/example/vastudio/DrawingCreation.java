package com.example.vastudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class DrawingCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_creation);

        EditText editTextArtistName = findViewById(R.id.editTextArtistName);
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artist = editTextArtistName.getText().toString();
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                String price = editTextPrice.getText().toString();

                Intent intent = new Intent(DrawingCreation.this, PaintCanvas.class);
                intent.putExtra("artist", artist);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });
    }
}
