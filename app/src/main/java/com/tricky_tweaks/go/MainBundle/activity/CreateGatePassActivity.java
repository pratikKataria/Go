package com.tricky_tweaks.go.MainBundle.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.libraries.places.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tricky_tweaks.go.R;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateGatePassActivity extends AppCompatActivity {

    PlacesClient placesClient;

    TextView textViewSName;
    TextView textViewSID;
    TextView textViewSSem;
    TextView textViewSBranch;
    ProgressBar progressBar;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gate_pass);

//        String apiKey = "AIzaSyCd8oel1kfcDho4u10UbsNghKqEcwhxhbg";
//
//        if (!Places.isInitialized()) {
//            Places.initialize(CreateGatePassActivity.this, apiKey);
//        }
//
//        placesClient = Places.createClient(CreateGatePassActivity.this);
//
//        final AutocompleteSupportFragment autocompleteSupportFragment =
//                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        if (autocompleteSupportFragment != null) {
//            autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
//        }
//
//        TextView textView = findViewById(R.id.terror);
//        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                final LatLng latLng = place.getLatLng();
//                Log.e("Created Gate Pass", latLng.latitude+ "\n"+latLng.longitude +" " + latLng.toString());
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                textView.setText(status.getStatusMessage());
//                Toast.makeText(CreateGatePassActivity.this, "status " +status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        EditText from = findViewById(R.id.activity_gate_pass_et_from);
        EditText to = findViewById(R.id.activity_gate_pass_et_to);

        EditText duration = findViewById(R.id.activity_gate_pass_et_duration);
        EditText s_count = findViewById(R.id.activity_gate_pass_et_s_count);

        Spinner moderator = findViewById(R.id.activity_gate_pass_sp_moderator);
        EditText reason = findViewById(R.id.activity_gate_pass_et_reason);

        textViewSName = findViewById(R.id.activity_gate_pass_tv_s_name);
        textViewSID = findViewById(R.id.activity_gate_pass_tv_s_id);
        textViewSSem = findViewById(R.id.activity_gate_pass_tv_s_semester);
        textViewSBranch = findViewById(R.id.activity_gate_pass_tv_s_branch);

        progressBar = findViewById(R.id.activity_gate_pass_pb);
        progressBar2 = findViewById(R.id.progressBar);

        MaterialButton mb = findViewById(R.id.activity_gate_pass_mb_save);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.moderator,
                android.R.layout.simple_spinner_item
        );

        final String[] sModerator = {"other"};

        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        moderator.setAdapter(arrayAdapter);
        moderator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sModerator[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setStaticFields();

        mb.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            Map<String, Object> map = new HashMap<>();

            String s = getSaltString();
            map.put("/" + s + "/gp_id", s);
            map.put("/" + s + "/s_id", FirebaseAuth.getInstance().getUid());
            map.put("/" + s + "/gp_from", from.getText().toString());
            map.put("/" + s + "/gp_to", to.getText().toString());
            map.put("/" + s + "/gp_reason", reason.getText().toString());
            map.put("/" + s + "/gp_s_count", s_count.getText().toString());
            map.put("/" + s + "/gp_moderator", sModerator[0]);
            map.put("/" + s + "/gp_duration", duration.getText().toString());
            map.put("/" + s + "/gp_time", new Date().toString());
            map.put("/" + s + "/gp_status", -1);
            //-1 = pending // 0 = seen // 1 == accepted

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GatePass");
            databaseReference.updateChildren(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);


//                    Map<String, Object> map1 = new HashMap<>();
//                    map1.put("/student_passes/"+FirebaseAuth.getInstance().getUid()+"/"+s, true);
//                    databaseReference.updateChildren(map1);
                    Toast.makeText(CreateGatePassActivity.this, "successfull", Toast.LENGTH_SHORT).show();
                } else{
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
            });

            String salt = getSaltString();
            String uid = FirebaseAuth.getInstance().getUid();
            Map<String, Object> notificationMap = new HashMap<>();
            notificationMap.put(salt+"/from", FirebaseAuth.getInstance().getUid());
            notificationMap.put(salt+"/type", "pending");

            //all notification send to pratikkatariya786.pk@gmail.com;
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("rzhBwseySnf6l03okDFCpagJuSo1");
                reference.updateChildren(notificationMap).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(CreateGatePassActivity.this, "created notification", Toast.LENGTH_SHORT).show();
                    }
                });
        });
    }

    public void setStaticFields() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Students");
        if (FirebaseAuth.getInstance().getUid() != null) {
            progressBar.setVisibility(View.GONE);
            databaseReference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    if (map != null) {
                        textViewSName.setText(""+map.get("s_name"));
                        textViewSID.setText("uid: "+map.get("s_id"));
                        textViewSSem.setText(""+map.get("s_sem"));
                        textViewSBranch.setText(""+map.get("s_branch"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
