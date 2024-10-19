package com.example.vastudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class askRole extends AppCompatActivity {
    Button SaveRoleBtn;
    RadioButton Rolesel;
    RadioGroup Role;
    FirebaseAuth Auth;
    boolean valid;
    FirebaseDatabase database;
    String RoleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_role);

        Auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Role = findViewById(R.id.RoleGroup);
        Role.clearCheck();
        SaveRoleBtn = findViewById(R.id.RoleSelBtn);
        SaveRoleBtn.setOnClickListener(v -> {
            int selroleid = Role.getCheckedRadioButtonId();
            valid = checkRadioGroup(Role);
            if (selroleid!= -1) {
                Rolesel = findViewById(selroleid);
                RoleText = Rolesel.getText().toString(); // Use roleText here
            }
            if (RoleText.equals("Artist")) {
                startActivity(new Intent(askRole.this, artistPanel.class));
            } else if (RoleText.equals("Visitor")) {
                startActivity(new Intent(askRole.this, visitorPanel.class));
            }
            DatabaseReference userRef = database.getReference("Users").child(Auth.getCurrentUser().getUid());
            userRef.child("role").setValue(RoleText);
        });
    }
    private boolean checkRadioGroup(RadioGroup... radioGroups) {
        for (RadioGroup radioGroup : radioGroups) {
            if (radioGroup.getCheckedRadioButtonId()!= -1) {
                valid = true;
                break;
            }
        }
        return valid;
    }
}