package com.example.vastudio;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.vastudio.databinding.ActivityArtistPanelBinding;
import com.example.vastudio.fragments.Exhibition.Exhibition_List;
import com.example.vastudio.fragments.HomeFragment;
import com.example.vastudio.fragments.MessageFragments.ArtistMessageFragment;
import com.example.vastudio.fragments.PaintFragment;
import com.example.vastudio.fragments.ProfileFragments.ArtistProfileFragment;
import com.example.vastudio.fragments.SearchFragment;
import com.example.vastudio.fragments.ViewArtistBidFragment;
import com.example.vastudio.fragments.ViewPaintingsFragment;

public class artistPanel extends AppCompatActivity {
    ActivityArtistPanelBinding binding;
    private final int ARTIST_FRAME_LAYOUT = R.id.ArtistFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtistPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ViewCurrentPublisherPaintingFragment());


        binding.ArtistBottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id==R.id.home){
                replaceFragment(new ViewCurrentPublisherPaintingFragment());
            } else if (id==R.id.search) {
                replaceFragment(new SearchFragment());
            }else if (id==R.id.paint) {
                replaceFragment(new ViewArtistBidFragment());
             //   startActivity(new Intent(artistPanel.this,PaintCanvas.class));
            }else if (id==R.id.message) {
                replaceFragment(new Exhibition_List());
            }else if (id==R.id.profile) {
                replaceFragment(new ArtistProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(ARTIST_FRAME_LAYOUT, fragment);
        fragmentTransaction.commit();
    }
}