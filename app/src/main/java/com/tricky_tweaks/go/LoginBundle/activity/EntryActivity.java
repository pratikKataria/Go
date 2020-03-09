package com.tricky_tweaks.go.LoginBundle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.tricky_tweaks.go.LoginBundle.fragment.LoginFragment;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

public class EntryActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_entry_container, new LoginFragment()).commit();

    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.activity_entry_container, fragment);

        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }
}
