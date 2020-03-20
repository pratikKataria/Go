package com.tricky_tweaks.go.LoginBundle.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tricky_tweaks.go.MainBundle.activity.MainActivity;
import com.tricky_tweaks.go.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFormFragment extends Fragment {

    TextView userId;
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
    MaterialButton dateSelector;

    private int year, month, day;

    private static String sBranch = "EC", sSemester = "I";

    private void init_fields(View v) {
        userId = v.findViewById(R.id.fragment_detail_form_tv_uid);
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

        dateSelector = v.findViewById(R.id.fragment_detail_form_mb_date_picker);
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

        getUserId();

        saveButton.setOnClickListener(v -> {

            if (userName.getText().toString().isEmpty()) {
                userName.setError("should not be empty");
                userName.requestFocus();
                return;
            }

            if (roomNo.getText().toString().isEmpty()) {
                roomNo.setError("should not be empty");
                roomNo.requestFocus();
                return;
            }

            if (userPhoneNo.getText().toString().isEmpty()) {
                userPhoneNo.setError("should not be empty");
                userPhoneNo.requestFocus();
                return;
            }

//            if (userPhoneNo.getText().toString().length() >= 9) {
//                userPhoneNo.setError("must be 10");
//                userPhoneNo.requestFocus();
//                return;
//            }

            if (fatherName.getText().toString().isEmpty()) {
                fatherName.setError("should not be empty");
                fatherName.requestFocus();
                return;
            }

            if (fatherPhoneNumber.getText().toString().isEmpty()) {
                fatherPhoneNumber.setError("should not be empty");
                fatherPhoneNumber.requestFocus();
                return;
            }

//            if (fatherPhoneNumber.getText().toString().length() >= 10) {
//                fatherPhoneNumber.setError("must be 10");
//                fatherPhoneNumber.requestFocus();
//                return;
//            }

            if (address.getText().toString().isEmpty()) {
                address.setError("should not be empty");
                address.requestFocus();
                return;
            }

//            int age = year - Calendar.YEAR;
//
//            if (age > 16) {
//                dateSelector.setText("select proper age");
//                new Handler().postDelayed(() -> {dateSelector.setText("select dob");},1000);
//                return;
//            }

            Map<String, Object> map = new HashMap<>();

            map.put("s_id", FirebaseAuth.getInstance().getUid());
            map.put("s_name", userName.getText().toString());
            map.put("s_branch", sBranch);
            map.put("s_sem", sSemester);
            map.put("s_room_no", roomNo.getText().toString());
            map.put("s_phone_no", userPhoneNo.getText().toString());
            map.put("s_dob", dateSelector.getText().toString());
            map.put("s_father_name", fatherName.getText().toString());
            map.put("s_father_phone_no", fatherPhoneNumber.getText().toString());
            map.put("s_address", address.getText().toString());
            map.put("d_token", FirebaseInstanceId.getInstance().getToken());

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Students/"+FirebaseAuth.getInstance().getUid());
            rootRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Map<String, Object> map  = new HashMap();
                    map.put("s_dob/year", year);
                    map.put("s_dob/month", month);
                    map.put("s_dob/day", day);
                    rootRef.updateChildren(map);

                    Toast.makeText(getActivity(), " successfull ", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getContext() , MainActivity.class));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                }
            });
        });

//        saveButton.setOnClickListener(v -> {
//
//            showProgress();
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("s_name", userName.getText().toString());
//            map.put("s_phoneNo", userPhoneNo.getText().toString());
//
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Student/"+FirebaseAuth.getInstance().getUid());
//
//            databaseReference.updateChildren(map).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    hideProgress();
//                } else {
//                    hideProgress();
//                }
//            }).addOnFailureListener(e -> hideProgress());
//        });

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
                sBranch = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
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
                sSemester = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateSelector.setOnClickListener(v -> showDateSelector());
        return view;
    }

    private boolean getUserId() {
        if (FirebaseAuth.getInstance().getUid() != null) {
            userId.setText(FirebaseAuth.getInstance().getUid());
            return true;
        } else {
            return false;
        }
    }


    private void showDateSelector() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                R.style.Widget_Go_DatePicker,
                ((view, year1, month1, dayOfMonth) -> dateSelector.setText(dayOfMonth +"/" + (month1 + 1) + "/"+ year1)), year, month, day);
        datePickerDialog.show();
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
