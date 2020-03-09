package com.tricky_tweaks.go.LoginBundle.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.tricky_tweaks.go.NavigationHost;
import com.tricky_tweaks.go.R;

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
        ImageButton imageButton = view.findViewById(R.id.test_image_btn);
        imageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((NavigationHost) getActivity()).navigateTo(new SignUpFragment(), false);
                    }
                }
        );
        return view;
    }

}
