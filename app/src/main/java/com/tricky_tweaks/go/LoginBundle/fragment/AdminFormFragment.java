package com.tricky_tweaks.go.LoginBundle.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tricky_tweaks.go.MainBundle.activity.MainActivity;
import com.tricky_tweaks.go.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFormFragment extends Fragment {

    public AdminFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_form, container, false);

        EditText editTextName = view.findViewById(R.id.fragment_admin_form_et_username);
        EditText editTextModerator = view.findViewById(R.id.fragment_admin_form_et_moderator);
        EditText editTextPhone = view.findViewById(R.id.fragment_detail_admin_et_phone_no);

        ProgressBar progressBar = view.findViewById(R.id.fragment_admin_form_pb_end);

        MaterialButton materialButton = view.findViewById(R.id.fragment_admin_form_mb_save);

        materialButton.setOnClickListener(n -> {

            if (editTextName.getText().toString().isEmpty()) {
                editTextName.setError("should not be empty");
                editTextName.requestFocus();
                return;
            }

            if (editTextModerator.getText().toString().isEmpty()) {
                editTextModerator.setError("should not be empty");
                editTextModerator.requestFocus();
                return;
            }

            if (editTextPhone.getText().toString().isEmpty()) {
                editTextPhone.setError("should not be empty");
                editTextPhone.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            Map<String, Object> map = new HashMap<>();

            map.put("a_id", FirebaseAuth.getInstance().getUid());
            map.put("a_name", editTextName.getText().toString());
            map.put("a_phone_no", editTextPhone.getText().toString());
            map.put("a_moderator", editTextModerator.getText().toString());
            map.put("d_token", FirebaseInstanceId.getInstance().getToken());
            map.put("Admin", true);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Admin/"+FirebaseAuth.getInstance().getUid());
            rootRef.updateChildren(map).addOnCompleteListener(task -> {

                progressBar.setVisibility(View.GONE);


                Toast.makeText(getActivity(), " data uploaded successfull ", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getContext() , MainActivity.class));

            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            });
        });


        return view;
    }
}
