package com.tricky_tweaks.go.LoginBundle.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.service.autofill.VisibilitySetterAction;
import android.text.style.CharacterStyle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    RadioGroup genderGroup;
    RadioButton radioButton;
    EditText roomNo;
    EditText userPhoneNo;
    EditText fatherName;
    EditText fatherPhoneNumber;
    EditText address;
    MaterialButton saveButton;

    ProgressBar start_pb;
    ProgressBar end_pb;

    TextView genderSelected;

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

        genderGroup = v.findViewById(R.id.fragment_detail_form_rg_gender);
        genderSelected = v.findViewById(R.id.fragment_detail_form_tv_gen_selected);
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

        genderSelected.setText("Gender selected: male");
        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = view.findViewById(checkedId);
            genderSelected.setText("Gender selected: " + radioButton.getText().toString());
        });

        TextView selectedBranch = view.findViewById(R.id.fragment_detail_form_tv_branch_selected);
        selectedBranch.setText("Branch selected: EC");
        Spinner spinner = view.findViewById(R.id.fragment_detail_form_sp_branch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.branch,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBranch.setText("Branch selected: " + parent.getItemAtPosition(position).toString());
                Toast.makeText(getContext(), "selected"+ parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView semesterBranch = view.findViewById(R.id.fragment_detail_form_tv_semester_selected);
        semesterBranch.setText("Semester selected: I");
        Spinner semesterSpinner = view.findViewById(R.id.fragment_detail_form_sp_semester);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.semester,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(arrayAdapter);
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semesterBranch.setText("Semester selected: " + parent.getItemAtPosition(position).toString());
                Toast.makeText(getContext(), "selected"+ parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
