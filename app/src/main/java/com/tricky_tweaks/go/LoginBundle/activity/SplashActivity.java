package com.tricky_tweaks.go.LoginBundle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tricky_tweaks.go.DataModel.GatePassData;
import com.tricky_tweaks.go.FirebaseCallback;
import com.tricky_tweaks.go.MainBundle.activity.MainActivity;
import com.tricky_tweaks.go.R;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    public static final int WELCOME_ACTIVITY = 0;
    public static final int ENTRY_ACTIVITY = 1;
    public static final int MAIN_ACTIVITY = 2;

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences("AppSettingPrefs", 0);
        boolean isFirstRun = preferences.getBoolean("FIRST_RUN", true);
        boolean isAdmin = preferences.getBoolean("IS_ADMIN", false);

        if (isFirstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FIRST_RUN", false);
            editor.apply();

            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));

        } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (isAdmin) {
                isDocPresent("Admin", FirebaseAuth.getInstance().getUid(), "a_name", new FirebaseCallback() {
                    @Override
                    public void isExist(boolean value) {
                        if (value) {
                            startActivityOnResult(MAIN_ACTIVITY, 1);
                        } else {
                            startActivityOnResult(ENTRY_ACTIVITY, 2);
                        }
                    }

                    @Override
                    public void getList(ArrayList<GatePassData> listData) {

                    }
                });
            } else {
                isDocPresent("Students", FirebaseAuth.getInstance().getUid(), "s_name", new FirebaseCallback() {
                    @Override
                    public void isExist(boolean value) {
                        if (value) {
                            startActivityOnResult(MAIN_ACTIVITY, 1);
                        } else {
                            startActivityOnResult(ENTRY_ACTIVITY, 1);
                        }
                    }

                    @Override
                    public void getList(ArrayList<GatePassData> listData) {

                    }
                });
            }
        } else {
            startActivity(new Intent(SplashActivity.this, EntryActivity.class));
        }

    }

    private void isDocPresent(String path, String id, String var, final FirebaseCallback firebaseCallback) {
        DatabaseReference documentReference = FirebaseDatabase.getInstance().getReference(path + "/" + id).child(var);
        final Integer[] isExist = new Integer[1];

        documentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue(String.class) != null) {
                    firebaseCallback.isExist(true);
                }
                else {
                    firebaseCallback.isExist(false);
                }

                Log.e("SplashActivity", "document snp : " + isExist[0]);

                Toast.makeText(SplashActivity.this, "" + dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void startActivityOnResult(int result, int event) {
        switch (result) {
            case WELCOME_ACTIVITY:
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                }, 1200);
                break;
            case ENTRY_ACTIVITY:
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashActivity.this, EntryActivity.class).putExtra("EVENT", event));
                }, 1200);
                break;
            case MAIN_ACTIVITY:
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

//                new Handler().postDelayed(() -> {
//                }, 1200);
                break;
        }
    }

}
