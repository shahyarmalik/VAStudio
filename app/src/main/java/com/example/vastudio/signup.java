package com.example.vastudio;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class signup extends AppCompatActivity {


    EditText Name, Email, Username, Age, CellNumber, Password;
    TextView loginRedirectText;
    RadioGroup gender, role;
    RadioButton gendersel, rolesel;
    Button signupButton;
    boolean valid;
    FirebaseAuth fAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String genderText, roleText; // Add these lines

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        fAuth = FirebaseAuth.getInstance();
        Name = findViewById(R.id.Signup_Name);
        Email = findViewById(R.id.Signup_EmailAddress);
        Username = findViewById(R.id.Signup_UserName);
        Password = findViewById(R.id.Signup_Password);
        CellNumber = findViewById(R.id.Signup_CellNumber);
        gender = findViewById(R.id.gender_group);
        gender.clearCheck();
        Age = findViewById(R.id.Signup_age);
        role = findViewById(R.id.role_group);
        role.clearCheck();
        signupButton = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.loginRedirect);
        signupButton.setOnClickListener(v -> {

            checkField(Username);
            checkField(Email);
            checkField(Password);
            checkField(Age);
            checkField(CellNumber);
            checkField(Name);
            valid = checkRadioGroup(gender, role);
            int selgenderid = gender.getCheckedRadioButtonId();
            if (selgenderid != -1) {
                gendersel = findViewById(selgenderid);
                genderText = gendersel.getText().toString(); // Use genderText here
            }
            int selroleid = role.getCheckedRadioButtonId();
            if (selroleid != -1) {
                rolesel = findViewById(selroleid);
                roleText = rolesel.getText().toString(); // Use roleText here
            }

            if (valid) {
                if (CellNumber.getText().toString().length() <= 10) {
                    CellNumber.setError("Cell number must be at least above 10 digits long");
                    valid = false;
                } else {
                    fAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnSuccessListener(authResult -> {
                        FirebaseUser user = fAuth.getCurrentUser();
                        Toast.makeText(signup.this, "Account Created", Toast.LENGTH_SHORT).show();
                        DatabaseReference df = database.getReference("Users").child(Objects.requireNonNull(user).getUid());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("Name", Name.getText().toString());
                        userInfo.put("Email", Email.getText().toString());
                        userInfo.put("Username", Username.getText().toString());
                        userInfo.put("Age", Age.getText().toString());
                        userInfo.put("Gender", genderText);
                        userInfo.put("CellNumber", CellNumber.getText().toString());
                        userInfo.put("Role", roleText);
                        userInfo.put("Id",fAuth.getCurrentUser().getUid());
                        userInfo.put("ImageUrl","default");
                        userInfo.put("isUser", "1");


                        df.setValue(userInfo);

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }).addOnFailureListener(e -> Toast.makeText(signup.this, "Account Creation Failed", Toast.LENGTH_SHORT).show());
                }
            }

        });
        loginRedirectText.setOnClickListener(View -> {
            Intent intent = new Intent(signup.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else if (textField.getId() == R.id.Signup_CellNumber) {
            String cellNumber = textField.getText().toString();
            if (cellNumber.length() <= 10) {
                textField.setError("Cell number must be at least above 10 digits long");
                valid = false;
            } else {
                valid = true;
            }
        } else if (textField.getId() == R.id.Signup_Password) {
            String password = textField.getText().toString();
            Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$");
            Matcher matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                textField.setError("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit.");
                valid = false;
            } else {
                valid = true;
            }
        } else {
            valid = true;
        }
    }
    private boolean checkRadioGroup(RadioGroup... radioGroups) {
        for (RadioGroup radioGroup : radioGroups) {
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                valid = true;
                break;
            }
        }

        return valid;
    }
}
