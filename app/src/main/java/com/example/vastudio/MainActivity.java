package com.example.vastudio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextView signupRedirectText;

    EditText Email, Password;
    Button loginButton;
    Button Google;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 20;
    boolean valid=false;


    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuth(account.getIdToken());
                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("goglesign",e.getMessage());
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

/*
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                                String isAdmin = snapshot.child("isAdmin").getValue(String.class);
                                                String isUser = snapshot.child("isUser").getValue(String.class);
                                                if ("1".equalsIgnoreCase(isAdmin)) {
                                                        startActivity(new Intent(getApplicationContext(), adminPanel.class));
                                                        finish();
                                                    } else if ("1".equalsIgnoreCase(isUser)) {
                                                        startActivity(new Intent(getApplicationContext(), askRole.class));
                                                        finish();
                                                    }
                                            } else {
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finish();
                                            }
                                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("TAG", "onCancelled", error.toException());
                                    }
            });
                    }
*/


        Email =findViewById(R.id.login_Email);
        Password =findViewById(R.id.login_Password);
        signupRedirectText =findViewById(R.id.signupRedirect);
        loginButton =findViewById(R.id.btnSignin);
        Google = findViewById(R.id.bthGoogle);
        auth = FirebaseAuth.getInstance();
        Button phone = findViewById(R.id.PhoneLogin);
        phone.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, phoneLogin.class);
            startActivity(intent);
        });



        Google.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.app_name))
                    .requestEmail().build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            GoogleSignIn();
        });


        loginButton.setOnClickListener(view -> {
            if (!valid) {
                ProgressDialog progressDialog=new ProgressDialog(this);
                progressDialog.setMessage("wait for login");
                progressDialog.onStart();


                auth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                        .addOnSuccessListener(authResult -> {
                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            checkUserAccessLevel(Objects.requireNonNull(authResult.getUser()).getUid());
                            Log.e("valid", authResult.toString());
                            valid = true; // Set valid to true after successful sign-in
                        })
                        .addOnFailureListener(e -> {
                            Log.e("valid", e.toString());
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(MainActivity.this, "You are already logged in", Toast.LENGTH_SHORT).show();
            }
        });
        signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, signup.class);
            startActivity(intent);
        });
    }

    private void GoogleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("gogle",e.getMessage());
            }
        }
    }

    private void checkUserAccessLevel(String uid) {
        database.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String isAdmin = snapshot.child("isAdmin").getValue(String.class);
                    String isUser = snapshot.child("isUser").getValue(String.class);
                    String RoleText = null;
                    if (snapshot.hasChild("Role")) {
                        RoleText = snapshot.child("Role").getValue(String.class);
                    }

                    if ("1".equalsIgnoreCase(isAdmin)) {
                        startActivity(new Intent(getApplicationContext(), adminPanel.class));
                        finish();
                    } else if ("1".equalsIgnoreCase(isUser)) {
                        if (RoleText != null && RoleText.equals("Artist")) {
                            startActivity(new Intent(MainActivity.this, artistPanel.class));
                        } else if (RoleText != null && RoleText.equals("Visitor")) {
                            startActivity(new Intent(MainActivity.this, visitorPanel.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), askRole.class));
                            finish();
                        }
                    }
                } else {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled", error.toException());
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String isAdmin = snapshot.child("isAdmin").getValue(String.class);
                        String isUser = snapshot.child("isUser").getValue(String.class);
                        String RoleText = null;
                        if (snapshot.hasChild("Role")) {
                            RoleText = snapshot.child("Role").getValue(String.class);
                        }
                        if ("1".equalsIgnoreCase(isAdmin)) {
                            startActivity(new Intent(getApplicationContext(), adminPanel.class));
                            finish();
                        } else if ("1".equalsIgnoreCase(isUser)) {
                            if (RoleText != null && RoleText.equals("Artist")) {
                                startActivity(new Intent(MainActivity.this, artistPanel.class));
                            } else if (RoleText != null && RoleText.equals("Visitor")) {
                                startActivity(new Intent(MainActivity.this, visitorPanel.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), askRole.class));
                                finish();
                            }
                        }
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("TAG", "onCancelled", error.toException());
                }
            });
        }
    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user!= null) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("name", user.getDisplayName());
                            map.put("profile", Objects.requireNonNull(user.getPhotoUrl()).toString());
                            database.child(user.getUid()).setValue(map, (error, ref) -> {
                                if (error!= null) {
                                    Log.e("TAG", "Failed to write user data to database: " + error.getMessage());
                                } else {
                                    Log.d("TAG", "User data written to database successfully.");
                                }
                            });
                        }
                        Intent intent = new Intent(MainActivity.this, askRole.class); // Corrected intent declaration
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }





}