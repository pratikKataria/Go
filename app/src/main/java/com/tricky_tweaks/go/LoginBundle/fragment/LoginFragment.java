package com.tricky_tweaks.go.LoginBundle.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment{

    public static final int RC_SIGN_IN = 234;

    GoogleSignInClient googleSignInClient;

    FirebaseAuth auth;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


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

        ImageButton imageButton = view.findViewById(R.id.fragment_login_ib_google);
        imageButton.setOnClickListener(
                v -> {
                        signInWithGoogle();
                }
        );

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                 GoogleSignInAccount account = task.getResult(ApiException.class);

                 firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ((NavigationHost)getActivity()).navigateTo(new SignUpFragment(), false);
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
