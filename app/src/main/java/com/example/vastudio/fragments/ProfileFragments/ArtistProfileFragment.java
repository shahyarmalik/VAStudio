package com.example.vastudio.fragments.ProfileFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vastudio.MainActivity;
import com.example.vastudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ArtistProfileFragment extends Fragment {

    TextView artist_name,artist_userName,artist_email,artist_gender,artist_cellNo,artist_role,artist_age;

    Button artistLogout,giveFeedback;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_artist_profile, container, false);
        artist_name=view.findViewById(R.id.artist_name);
        artist_email=view.findViewById(R.id.artist_email);
        artist_gender=view.findViewById(R.id.artist_gender);
        artist_userName=view.findViewById(R.id.artist_userName);
        artist_age=view.findViewById(R.id.artist_age);
        artist_cellNo=view.findViewById(R.id.artist_cell);
        artist_role=view.findViewById(R.id.artist_roll);
        artistLogout=view.findViewById(R.id.artist_logoutButton);

        artistLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();

            }
        });
        createDb();
        return view;
    }



    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Log Out")
                .setMessage("Are you sure you want to Sign out?")
                .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
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
        getActivity().finish();
    }

    public void createDb(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("Name").getValue().toString();
                String email=snapshot.child("Email").getValue().toString();
                String username=snapshot.child("Username").getValue().toString();
                String age=snapshot.child("Age").getValue().toString();
                String cellNo=snapshot.child("CellNumber").getValue().toString();
                String gender=snapshot.child("Gender").getValue().toString();
                String role=snapshot.child("Role").getValue().toString();

                if(role.equals("Artist")){
                    artist_name.setText(name);
                    artist_email.setText(email);
                    artist_userName.setText(username);
                    artist_age.setText(age);
                    artist_gender.setText(gender);
                    artist_role.setText(role);
                    artist_cellNo.setText(cellNo);
                }
                else{
                    Toast.makeText(getView().getContext(), "check on another", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}