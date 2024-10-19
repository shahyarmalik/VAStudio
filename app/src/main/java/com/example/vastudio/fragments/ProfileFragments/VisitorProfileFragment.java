package com.example.vastudio.fragments.ProfileFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vastudio.Feedback.FeedbackActivity;
import com.example.vastudio.MainActivity;
import com.example.vastudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class VisitorProfileFragment extends Fragment {


    Button signout,giveFeedback;

    TextView v_name,v_userName,v_email,v_gender,v_cellNo,v_role,v_age;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visitor_profile, container, false);
        signout = view.findViewById(R.id.v_logoutButton);

        v_name=view.findViewById(R.id.v_name);
        v_email=view.findViewById(R.id.v_email);
        v_gender=view.findViewById(R.id.v_gender);
        v_userName=view.findViewById(R.id.v_userName);
        v_age=view.findViewById(R.id.v_age);
        v_cellNo=view.findViewById(R.id.v_cell);
        v_role=view.findViewById(R.id.v_roll);
        giveFeedback=view.findViewById(R.id.feedback_give);

        createDb();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogoutButtonClicked(signout);
            }
        });
        giveFeedback.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), FeedbackActivity.class));

        });
        return view;
    }

    public void onLogoutButtonClicked(View view) {
       showLogoutDialog();
    }

    public void createDb() {
        FirebaseAuth fAuth=FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");


     //   Log.e("id",user.getUid());
        databaseReference.child(Objects.requireNonNull(user).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Name").getValue().toString();
                String email = snapshot.child("Email").getValue().toString();
                String username = snapshot.child("Username").getValue().toString();
                String age = snapshot.child("Age").getValue().toString();
                String cellNo = snapshot.child("CellNumber").getValue().toString();
                String gender = snapshot.child("Gender").getValue().toString();
                String role = snapshot.child("Role").getValue().toString();

                if (role.equals("Visitor")) {
                    v_name.setText(name);
                    v_email.setText(email);
                    v_userName.setText(username);
                    v_age.setText(age);
                    v_gender.setText(gender);
                    v_role.setText(role);
                    v_cellNo.setText(cellNo);
                } else {
                    Toast.makeText(getView().getContext(), "check on another", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOutUser();
                    }


                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish(); // Optional: Close the activity after logging out
    }
}