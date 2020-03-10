package com.tricky_tweaks.go.LoginBundle.fragment;


import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText editTextEmail = view.findViewById(R.id.fragment_login_et_email);
        EditText editTextPassword = view.findViewById(R.id.fragment_login_et_password);
        MaterialButton materialButtonLogin = view.findViewById(R.id.fragment_login_mb_login);

        materialButtonLogin.setOnClickListener(v -> {
            if (editTextEmail.getText().toString().isEmpty()) {
                editTextEmail.setError("field should not be empty");
                editTextEmail.requestFocus();
                return;
            }

            if (!isEmailValid(editTextEmail.getText().toString()))
                return;

            if (editTextPassword.getText().toString().isEmpty()) {
                editTextPassword.setError("password should not be empty");
                editTextPassword.requestFocus();
                return;
            }

            if (!isPasswordValid(editTextPassword.getText())) {
                editTextPassword.setError("password is greater then 8");
                editTextPassword.requestFocus();
                return;
            }

            loginUsingEmailPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString());

//            editTextPassword.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                    if (isPasswordValid(editTextPassword.getText())) {
//                        editTextPassword.setError(null);
//
//                    }
//                    return false;
//                }
//            });

        });

        ImageButton imageButton = view.findViewById(R.id.test_image_btn);
        imageButton.setOnClickListener(
                v -> ((NavigationHost) getActivity()).navigateTo(new SignUpFragment(), false)
        );

        return view;
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(Editable input) {
        return input != null && input.length() >= 8;
    }


    private void loginUsingEmailPassword(String toString, String toString1) {
    }

}
