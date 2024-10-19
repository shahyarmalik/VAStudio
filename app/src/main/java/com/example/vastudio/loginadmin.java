package com.example.vastudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class loginadmin extends AppCompatActivity {
    private EditText emailText;
    private EditText passText;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);

        emailText = findViewById(R.id.email);
        passText = findViewById(R.id.password);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.btnSignIn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passText.getText().toString();
//                if (email.equals("BC180401308") && password.equals("803104081bc")) {
//                    startActivity(new Intent(loginadmin.this, AdminDashboard.class));
//                    finish();
//                } else {
//                    Toast.makeText(loginadmin.this, "Please input valid email or password",
//                            Toast.LENGTH_SHORT).show();
//                }
                if (email.equals("admin") && password.equals("admin")) {
                    startActivity(new Intent(loginadmin.this, adminPanel.class));
                    finish();
                } else {
                    Toast.makeText(loginadmin.this, "email or password not valid",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
