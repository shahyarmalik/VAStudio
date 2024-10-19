package com.example.vastudio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button userbtn=findViewById(R.id.userBtn);
       Button admin=findViewById(R.id.adminBtn);
       userbtn.setOnClickListener(view -> {
           startActivity(new Intent(this,MainActivity.class));
       });
       admin.setOnClickListener(view -> {
           startActivity(new Intent(this, loginadmin.class));
       });
    }
}