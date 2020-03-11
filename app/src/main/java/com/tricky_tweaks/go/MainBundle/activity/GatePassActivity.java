package com.tricky_tweaks.go.MainBundle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tricky_tweaks.go.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GatePassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass);

        EditText from = findViewById(R.id.activity_gate_pass_et_from);
        EditText to = findViewById(R.id.activity_gate_pass_et_to);

        EditText count = findViewById(R.id.activity_gate_pass_et_student_count);
        EditText reason = findViewById(R.id.activity_gate_pass_et_reason);

        MaterialButton mb = findViewById(R.id.activity_gate_pass_mb_save);

        mb.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();

            String s = getSaltString();
            map.put("/"+s+"/gp_id", s);
            map.put("/"+s+"/s_id", FirebaseAuth.getInstance().getUid());
            map.put("/"+s+"/gp_from", from.getText().toString());
            map.put("/"+s+"/gp_to", to.getText().toString());
            map.put("/"+s+"/gp_s_count", count.getText().toString());
            map.put("/"+s+"/gp_reason", reason.getText().toString());
            map.put("/"+s+"/gp_time", new Date());
            map.put("/"+s+"/gp_status", 0);
            //-1 = pending // 0 = seen // 1 == accepted

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GatePass");
            databaseReference.updateChildren(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("/student_passes/"+FirebaseAuth.getInstance().getUid()+"/"+s, true);
                    databaseReference.updateChildren(map1);
                    Toast.makeText(GatePassActivity.this, "successfull", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {

            });
        });
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
