package com.example.vastudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneLogin extends AppCompatActivity {
    EditText phone, otp;
    Button btngenOTP, btnverify;
    FirebaseAuth mAuth;
    String verificationID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        phone = findViewById(R.id.PhoneNumber);
        otp = findViewById(R.id.OTPtxt);
        btngenOTP = findViewById(R.id.GenerateBtn);
        btnverify = findViewById(R.id.VerificationBtn);
        mAuth = FirebaseAuth.getInstance();
        btngenOTP.setOnClickListener(v -> {
            if (TextUtils.isEmpty(phone.getText().toString())){
                Toast.makeText(phoneLogin.this, "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
            }else {
                String number = phone.getText().toString();
                sendverificationcode(number);
            }


        });
        btnverify.setOnClickListener(v -> {
            if (TextUtils.isEmpty(phone.getText().toString())) {
                Toast.makeText(phoneLogin.this,"Wrong OTP Entered",Toast.LENGTH_SHORT).show();
            }else {
                verifycode(otp.getText().toString());
            }
        });
    }


    private void verifycode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,Code);
        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(phoneLogin.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(phoneLogin.this,askRole.class));
                    }
                });
    }
    @Override
    protected  void onStart(){
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null)
        {
            startActivity(new Intent(phoneLogin.this,askRole.class));
            finish();
        }
    }

    private void sendverificationcode(String phoneNumber) {    PhoneAuthOptions options =
            PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber("+92"+phoneNumber)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // (optional) Activity for callback binding
                    // If no activity is passed, reCAPTCHA verification can not be used.
                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                    .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential)
        {
            final String code =credential.getSmsCode();
            if (code!=null){
                verifycode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(phoneLogin.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            verificationID = s;
            Toast.makeText(phoneLogin.this, "Code sent", Toast.LENGTH_SHORT).show();
            btnverify.setEnabled(true);
        }


    };
}