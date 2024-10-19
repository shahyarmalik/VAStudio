package com.example.vastudio;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.vastudio.databinding.ActivityVisitorPanelBinding;
import com.example.vastudio.fragments.BidArtworkActivity;
import com.example.vastudio.fragments.Exhibition.Exhibition_List;
import com.example.vastudio.fragments.HomeFragment;
import com.example.vastudio.fragments.ProfileFragments.VisitorProfileFragment;
import com.example.vastudio.fragments.ViewArtistBidFragment;
import com.example.vastudio.fragments.ViewMyBidFragment;
import com.example.vastudio.fragments.ViewPaintingsFragment;

public class visitorPanel extends AppCompatActivity {
    ActivityVisitorPanelBinding binding;
    private final int VISITOR_FRAME_LAYOUT = R.id.VisitorFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitorPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ViewVisitorPaintingFragment());
        binding.VisitorbottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new ViewVisitorPaintingFragment());
            } else
//                if (id == R.id.search) {
//                replaceFragment(new ViewVisitorPaintingFragment());
//            } else
                if (id == R.id.bid) {

               replaceFragment(new ViewMyBidFragment());
            } else if (id == R.id.message) {
                replaceFragment(new Exhibition_List());
            } else if (id == R.id.profile) {
                replaceFragment(new VisitorProfileFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(VISITOR_FRAME_LAYOUT, fragment);
        fragmentTransaction.commit();

    }
}