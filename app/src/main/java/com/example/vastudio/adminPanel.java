package com.example.vastudio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import com.example.vastudio.Feedback.FeedbackListFragment;
import com.example.vastudio.databinding.ActivityAdminPanelBinding;
import com.example.vastudio.fragments.Exhibition.Exhibition_List;
import com.example.vastudio.fragments.HomeFragment;
import com.example.vastudio.fragments.ProfileFragments.AdminProfileFragment;
import com.example.vastudio.fragments.SearchFragment;
import com.example.vastudio.fragments.ViewPaintingsFragment;

public class adminPanel extends AppCompatActivity {
    ActivityAdminPanelBinding binding;
    private final int ADMIN_FRAME_LAYOUT = R.id.AdminFrameLayout;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ViewPaintingsFragment());

        binding.AdminBottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id==R.id.homeAdmin){
                replaceFragment(new ViewPaintingsFragment());
            } else if (id==R.id.feedback) {
                replaceFragment(new FeedbackListFragment());
            }else if (id==R.id.message) {
                replaceFragment(new Exhibition_List());
            }else if (id==R.id.profile) {
                replaceFragment(new AdminProfileFragment());
            }
            return true;
                });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(ADMIN_FRAME_LAYOUT, fragment);
        fragmentTransaction.commit();
    }

}