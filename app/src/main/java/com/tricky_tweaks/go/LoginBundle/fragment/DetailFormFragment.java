package com.tricky_tweaks.go.LoginBundle.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.service.autofill.VisibilitySetterAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tricky_tweaks.go.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFormFragment extends Fragment {

    EditText userName;
    RadioGroup gender;
    EditText roomNo;
    EditText userPhoneNo;
    EditText fatherName;
    EditText fatherPhoneNumber;
    EditText address;
    MaterialButton saveButton;

    ProgressBar start_pb;
    ProgressBar end_pb;

    private void init_fields(View v) {
        userName = v.findViewById(R.id.fragment_detail_form_et_username);
        roomNo = v.findViewById(R.id.fragment_detail_form_et_room_no);
        userPhoneNo = v.findViewById(R.id.fragment_detail_form_et_phone_no);
        fatherName = v.findViewById(R.id.fragment_detail_form_et_father_name);
        fatherPhoneNumber = v.findViewById(R.id.fragment_detail_form_et_father_phone_no);
        address = v.findViewById(R.id.fragment_detail_form_et_address);

        saveButton = v.findViewById(R.id.fragment_detail_form_mb_save);
        start_pb = v.findViewById(R.id.fragment_detail_form_pb_start);
        end_pb = v.findViewById(R.id.fragment_detail_form_pb_end);
    }

    public DetailFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_form, container, false);

        init_fields(view);

        saveButton.setOnClickListener(v -> {

            showProgress();

            Map<String, Object> map = new HashMap<>();
            map.put("s_name", userName.getText().toString());
            map.put("s_phoneNo", userPhoneNo.getText().toString());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Student/"+FirebaseAuth.getInstance().getUid());

            databaseReference.updateChildren(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    hideProgress();
                } else {
                    hideProgress();
                }
            }).addOnFailureListener(e -> hideProgress());
        });
        return view;
    }

    private void showProgress() {
        start_pb.setVisibility(View.VISIBLE);
        end_pb.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        start_pb.setVisibility(View.GONE);
        end_pb.setVisibility(View.GONE);
    }

}
