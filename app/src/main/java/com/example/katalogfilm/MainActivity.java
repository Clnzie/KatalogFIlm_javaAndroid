package com.example.katalogfilm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.katalogfilm.databinding.ActivityMainBinding;
import com.example.katalogfilm.ui.akun.AkunFragment;
import com.example.katalogfilm.ui.fav.FavFragment;
import com.example.katalogfilm.ui.home.HomeFragment;
import com.example.katalogfilm.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemID = item.getItemId();

            if (itemID == R.id.Home) {
                replaceFragment(new HomeFragment());
            } else if (itemID ==  R.id.Search) {
                replaceFragment(new SearchFragment());
            } else if (itemID == R.id.Fav) {
                replaceFragment(new FavFragment());
            } else {
                replaceFragment(new AkunFragment());
            }

            return true;
        });
    }

    private void replaceFragment (Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }
}