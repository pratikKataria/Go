package com.tricky_tweaks.go.LoginBundle.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment{

    public static final int RC_SIGN_IN = 234;

    private ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.fragment_login_pb);

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
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        new Handler().postDelayed(()->{
                            progressBar.setVisibility(View.GONE);
                            ((NavigationHost)getActivity()).navigateTo(new DetailFormFragment(), false);
                        }, 1000);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "error creating account", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void signInWithGoogle() {
        progressBar.setVisibility(View.VISIBLE);
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

    private void checkUserInfo(String getUid) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference studentNode = rootRef.child("Student").child(getUid);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("LOGIN FRAGMENT" , value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
