package com.tricky_tweaks.go.LoginBundle.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tricky_tweaks.go.LoginBundle.fragment.DetailFormFragment;
import com.tricky_tweaks.go.LoginBundle.fragment.LoginFragment;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

public class EntryActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        int event = getIntent().getIntExtra("EVENT", 0);

        Log.e("EntryActivity", event+"");

        switch (event) {
            case 0:
                getSupportFragmentManager().beginTransaction().add(R.id.activity_entry_container, new LoginFragment()).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().add(R.id.activity_entry_container, new DetailFormFragment()).commit();
                break;
        }


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
