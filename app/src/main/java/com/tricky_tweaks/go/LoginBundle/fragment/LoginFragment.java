package com.tricky_tweaks.go.LoginBundle.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.okhttp.internal.DiskLruCache;
import com.tricky_tweaks.go.MainBundle.activity.MainActivity;
import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment{

    private static final int RC_SIGN_IN = 234;
    private boolean isAdminClicked = false;

    private ProgressBar progressBar;
    private MaterialButton materialButtonLogin;
    private ImageButton imageButton;
    private TextView textError;
    private AlertDialog dialog;

    private SharedPreferences sharedPreferences;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    private void init_fields(View view ) {
        materialButtonLogin = view.findViewById(R.id.fragment_login_mb_login);
        progressBar = view.findViewById(R.id.fragment_login_pb);
        imageButton = view.findViewById(R.id.fragment_login_ib_google);
        textError = view.findViewById(R.id.fragment_login_tv_error);


        sharedPreferences = context.getSharedPreferences("AppSettingPrefs",0);


        auth = FirebaseAuth.getInstance();
    }

    private void init_Google_GSO() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        init_fields(view);

        init_Google_GSO();

        materialButtonLogin.setOnClickListener(v -> {
            isAdminClicked = true;
            showAlertDialogBox();
        });

        imageButton.setOnClickListener(v -> signInWithGoogle());

        return view;
    }

    public void showAlertDialogBox() {

        View alertLayout = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_request, null);
        final EditText mPassEditText = alertLayout.findViewById(R.id.alert_dialog_et_pass);
        final MaterialButton continueBtn = alertLayout.findViewById(R.id.alert_dialog_mb_continue);
        final MaterialButton cancelBtn = alertLayout.findViewById(R.id.alert_dialog_mb_cancel);
        final ProgressBar progressBar = alertLayout.findViewById(R.id.progressbar);
        final ImageButton googleIB = alertLayout.findViewById(R.id.alert_dialog_ib_google);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(alertLayout);
        builder.setCancelable(false);


        dialog = builder.create();

        continueBtn.setOnClickListener(n -> {
            if (mPassEditText.getText().toString().equals("")) {
                mPassEditText.setError("should not be empty");
                mPassEditText.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
            ref.child("password").child("login_pass").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.e("LOGING ", dataSnapshot.toString());

                    String getPass = dataSnapshot.getValue(String.class);
                    if (dataSnapshot.exists() && getPass != null && mPassEditText.getText().toString().equals(getPass)) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "correct", Toast.LENGTH_SHORT).show();

                        googleIB.setVisibility(View.VISIBLE);

                        googleIB.setOnClickListener(n -> {
                            signInWithGoogle();
                        });

                    } else {
                        googleIB.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mPassEditText.setText("");
                        Toast.makeText(getActivity(), "worng pass", Toast.LENGTH_SHORT).show();
                        Log.e("LOGING ", isAdminClicked+"");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    isAdminClicked = false;
                    Log.e("LOGING ", isAdminClicked+"");

                }
            });

        });

        cancelBtn.setOnClickListener(n -> {
            dialog.dismiss();
            isAdminClicked = false;

            Log.e("LOGING ", isAdminClicked+"");
            Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
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
                        if (isAdminClicked) {
                            dialog.dismiss();
                            imageButton.setVisibility(View.GONE);
                            Log.e("Login fragment", FirebaseAuth.getInstance().getAccessToken(true)+"");
                            checkUserInfo("Admin", FirebaseAuth.getInstance().getUid(), "a_name");
                            checkDeviceToken("Admin", FirebaseAuth.getInstance().getUid());
                        } else {
                            imageButton.setVisibility(View.VISIBLE);
                            Log.e("Login fragment", FirebaseAuth.getInstance().getAccessToken(true)+"");
                            checkUserInfo("Students", FirebaseAuth.getInstance().getUid(), "s_name");
                            checkDeviceToken("Students", FirebaseAuth.getInstance().getUid());
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "error creating account", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
            imageButton.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.GONE);
            showError(e.getMessage());
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void checkDeviceToken(String path, String id) {
        if (FirebaseAuth.getInstance().getUid() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            ref.child(id + "/d_token").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("LOGIn", dataSnapshot + "");
                    Log.e("LOGIN", "current token " + FirebaseInstanceId.getInstance().getToken());
                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        String token = dataSnapshot.getValue(String.class);
                        if (token != null && token.equals(FirebaseInstanceId.getInstance().getToken())) {
                            Activity activity = getActivity();
                            if (activity != null && isAdded()) {
                                Toast.makeText(getActivity(), "Token Verified", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ref.child(id + "/d_token").setValue(FirebaseInstanceId.getInstance().getToken(), (databaseError, databaseReference) -> Toast.makeText(getActivity(), "Token Changed", Toast.LENGTH_SHORT).show());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void signInWithGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void checkUserInfo(String path, String id, String var) {
        DatabaseReference studentNode = FirebaseDatabase.getInstance().getReference(path+"/"+ id).child(var);
        studentNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Login fragment", dataSnapshot.getValue() + "");
                if (dataSnapshot.getValue(String.class) == null) {
                    progressBar.setVisibility(View.GONE);

                    if (isAdminClicked) {
                            SharedPreferences.Editor  editor = sharedPreferences.edit();
                            editor.putBoolean("IS_ADMIN", true);
                            editor.apply();
                            ((NavigationHost) getActivity()).navigateTo(new AdminFormFragment(), false);
                    } else {
                        ((NavigationHost) getActivity()).navigateTo(new StudentFormFragment(), false);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

                    if(isAdminClicked) {
//                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppSettingPrefs",0);
                        SharedPreferences.Editor  editor = sharedPreferences.edit();
                        editor.putBoolean("IS_ADMIN", true);
                        editor.apply();

                        Activity activity = getActivity();
                        if (activity != null && isAdded()) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }

                    } else {
                        Activity activity = getActivity();
                        if (activity != null && isAdded()) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }   DatabaseReference studentNode = FirebaseDatabase.getInstance().getReference(path+"/"+ id).child(var);
        studentNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Login fragment", dataSnapshot.getValue() + "");
                if (dataSnapshot.getValue(String.class) == null) {
                    progressBar.setVisibility(View.GONE);

                    if (isAdminClicked) {
                            SharedPreferences.Editor  editor = sharedPreferences.edit();
                            editor.putBoolean("IS_ADMIN", true);
                            editor.apply();
                            ((NavigationHost) getActivity()).navigateTo(new AdminFormFragment(), false);
                    } else {
                        ((NavigationHost) getActivity()).navigateTo(new StudentFormFragment(), false);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

                    if(isAdminClicked) {
//                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppSettingPrefs",0);
                        SharedPreferences.Editor  editor = sharedPreferences.edit();
                        editor.putBoolean("IS_ADMIN", true);
                        editor.apply();

                        Activity activity = getActivity();
                        if (activity != null && isAdded()) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }

                    } else {
                        Activity activity = getActivity();
                        if (activity != null && isAdded()) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showError(String message) {
        textError.setText(message);
        new Handler().postDelayed(() -> textError.setText(""), 60000);
    }
}
