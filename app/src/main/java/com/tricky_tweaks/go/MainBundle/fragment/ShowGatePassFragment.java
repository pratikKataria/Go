package com.tricky_tweaks.go.MainBundle.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tricky_tweaks.go.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGatePassFragment extends Fragment {


    public ShowGatePassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_gate_pass, container, false);
    }

}
