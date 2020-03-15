package com.tricky_tweaks.go.MainBundle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.tricky_tweaks.go.MainBundle.fragment.ShowGatePassFragment;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;


public class MainActivity extends AppCompatActivity implements NavigationHost{

//    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
//    private String serverKey = "key="+"AAAAIvAbziU:APA91bFXPy1SVLIcV94vKk1uWB5MmrwPBRn-NOHghPxhgUNCwBrw9xCVB1UU3RfgQFXN7vRnaF_c1wyCWSjyFwj3QTxDJqSK4bV_tqsXlxlApk1Dem-Zz5vFWFAUKR1zUhMWdu0v_NlK";
//
//    private String contentType = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_container, new ShowGatePassFragment()).commit();


        MaterialButton mb = findViewById(R.id.activity_main_mb_gate_pass);
        mb.setOnClickListener(n-> {
            startActivity(new Intent(MainActivity.this, CreateGatePassActivity.class));
        });
//
//        MaterialButton mb1 = findViewById(R.id.activity_main_mb_leave_pass);
//        mb1.setOnClickListener(n-> {
//            DatabaseReference documentReference = FirebaseDatabase.getInstance().getReference("GatePass/");
//            documentReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getValue() != null)
//                    tv.setText(dataSnapshot.getValue().toString()+"");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        });

    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
