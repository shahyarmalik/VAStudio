package com.example.vastudio.fragments.ProfileFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vastudio.MainActivity;
import com.example.vastudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminProfileFragment extends Fragment {

    TextView adminName, adminEmail, adminAge, adminCell, adminRoll, adminGender, adminUserName;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        adminName = view.findViewById(R.id.admin_name);
        adminEmail = view.findViewById(R.id.admin_email);
        adminAge = view.findViewById(R.id.admin_age);
        adminCell = view.findViewById(R.id.admin_cell);
        adminGender = view.findViewById(R.id.admin_gender);
        adminRoll = view.findViewById(R.id.admin_roll);
        adminUserName = view.findViewById(R.id.admin_userName);

        createDb();
        // Find the logout button in the layout and set its onClickListener
        view.findViewById(R.id.admin_logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });



        return view;
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onLogOut();
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

    private void onLogOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish(); // Optional: Close the activity after logging out
    }

    public void createDb() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("Name").getValue().toString();
                String email = snapshot.child("Email").getValue().toString();
                String username = snapshot.child("").getValue().toString();
                String age = snapshot.child("Age").getValue().toString();
                String cellNo = snapshot.child("CellNumber").getValue().toString();
                String gender = snapshot.child("Gender").getValue().toString();
                String role = snapshot.child("Role").getValue().toString();

                if (role.equals("Admin")) {
                    adminName.setText(name);
                    adminEmail.setText(email);
                    adminUserName.setText(username);
                    adminAge.setText(age);
                    adminGender.setText(gender);
                    adminRoll.setText(role);
                    adminCell.setText(cellNo);
                }
                else {
                    Toast.makeText(getView().getContext(), "check on another", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}