package com.tricky_tweaks.go.MainBundle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tricky_tweaks.go.MainBundle.fragment.ShowGatePassFragment;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

import static com.airbnb.lottie.LottieDrawable.INFINITE;


public class MainActivity extends AppCompatActivity implements NavigationHost{

//    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
//    private String serverKey = "key="+"AAAAIvAbziU:APA91bFXPy1SVLIcV94vKk1uWB5MmrwPBRn-NOHghPxhgUNCwBrw9xCVB1UU3RfgQFXN7vRnaF_c1wyCWSjyFwj3QTxDJqSK4bV_tqsXlxlApk1Dem-Zz5vFWFAUKR1zUhMWdu0v_NlK";
//
//    private String contentType = "application/json";

    boolean isGone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieDrawable lottieDrawable = new LottieDrawable();
        LottieCompositionFactory.fromAsset(this, "fab_animation.json").addListener(
                result -> {
                   lottieDrawable.setComposition(result);
                   lottieDrawable.setScale(0.5F);
                   lottieDrawable.setRepeatMode(INFINITE);
                   lottieDrawable.pauseAnimation();
                }
        );



        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("MAINACT", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.e("MAINACT", token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_container, new ShowGatePassFragment()).commit();

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        MaterialButton mb = findViewById(R.id.activity_main_mb_gate_pass);
        MaterialButton mb1 = findViewById(R.id.activity_main_mb_leave_pass);

        fab.setImageDrawable(lottieDrawable);

        fab.setOnClickListener(n -> {
            if (isGone) {
                mb.setVisibility(View.VISIBLE);
                mb1.setVisibility(View.VISIBLE);
                isGone = false;
            } else {
                mb.setVisibility(View.GONE);
                mb1.setVisibility(View.GONE);
                isGone = true;
            }

        });


        mb.setOnClickListener(n-> {
            startActivity(new Intent(MainActivity.this, CreateGatePassActivity.class));
        });
//
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
