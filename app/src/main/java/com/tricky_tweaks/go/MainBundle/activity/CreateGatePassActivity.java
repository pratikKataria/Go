package com.tricky_tweaks.go.MainBundle.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.libraries.places.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tricky_tweaks.go.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateGatePassActivity extends AppCompatActivity {

    PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gate_pass);

        String apiKey = "AIzaSyCd8oel1kfcDho4u10UbsNghKqEcwhxhbg";

        if (!Places.isInitialized()) {
            Places.initialize(CreateGatePassActivity.this, apiKey);
        }

        placesClient = Places.createClient(CreateGatePassActivity.this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteSupportFragment != null) {
            autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        }

        TextView textView = findViewById(R.id.terror);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                Log.e("Created Gate Pass", latLng.latitude+ "\n"+latLng.longitude +" " + latLng.toString());
            }

            @Override
            public void onError(@NonNull Status status) {
                textView.setText(status.getStatusMessage());
                Toast.makeText(CreateGatePassActivity.this, "status " +status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        EditText from = findViewById(R.id.activity_gate_pass_et_from);
//        EditText to = findViewById(R.id.activity_gate_pass_et_to);
//
//        EditText duration = findViewById(R.id.activity_gate_pass_et_duration);
//        EditText s_count = findViewById(R.id.activity_gate_pass_et_s_count);
//
//        Spinner moderator = findViewById(R.id.activity_gate_pass_sp_moderator);
//        EditText reason = findViewById(R.id.activity_gate_pass_et_reason);
//
//        MaterialButton mb = findViewById(R.id.activity_gate_pass_mb_save);
//
//        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.moderator,
//                android.R.layout.simple_spinner_item
//        );
//
//        final String []sModerator = {"other"};
//
//        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        moderator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sModerator[0] = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        mb.setOnClickListener(v -> {
//            Map<String, Object> map = new HashMap<>();
//
//            String s = getSaltString();
//            map.put("/"+s+"/gp_id", s);
//            map.put("/"+s+"/s_id", FirebaseAuth.getInstance().getUid());
//            map.put("/"+s+"/gp_from", from.getText().toString());
//            map.put("/"+s+"/gp_to", to.getText().toString());
//            map.put("/"+s+"/gp_reason", reason.getText().toString());
//            map.put("/"+s+"/gp_s_count", s_count.getText().toString());
//            map.put("/"+s+"/gp_moderator", sModerator[0]);
//            map.put("/"+s+"/gp_duration", duration.getText().toString());
//            map.put("/"+s+"/gp_time", new Date().toString());
//            map.put("/"+s+"/gp_status", 0);
//            //-1 = pending // 0 = seen // 1 == accepted
//
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GatePass");
//            databaseReference.updateChildren(map).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
////                    Map<String, Object> map1 = new HashMap<>();
////                    map1.put("/student_passes/"+FirebaseAuth.getInstance().getUid()+"/"+s, true);
////                    databaseReference.updateChildren(map1);
//                    Toast.makeText(CreateGatePassActivity.this, "successfull", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(e -> {
//
//            });
//        });
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
