package com.tricky_tweaks.go.LoginBundle.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tricky_tweaks.go.LoginBundle.fragment.AdminFormFragment;
import com.tricky_tweaks.go.LoginBundle.fragment.LoginFragment;
import com.tricky_tweaks.go.LoginBundle.fragment.StudentFormFragment;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

public class EntryActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        int event = getIntent().getIntExtra("EVENT", -1);

        Log.e("EntryActivity", event+"");

        switch (event) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_entry_container, new LoginFragment()).commit();
                Log.e("EntryActivity", "LOGIN STARTED");
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_entry_container, new StudentFormFragment()).commit();
                Log.e("EntryActivity", "STUDENT FORM STARTED");
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_entry_container, new AdminFormFragment()).commit();
                Log.e("EntryActivity", "ADMIN FORM STARTED");
                break;
            case -1:
                Toast.makeText(this, "no fragment selected", Toast.LENGTH_SHORT).show();
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
