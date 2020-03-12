package com.tricky_tweaks.go.MainBundle.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.Distribution;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tricky_tweaks.go.FirebaseCallback;
import com.tricky_tweaks.go.GatePassData;
import com.tricky_tweaks.go.GatePassRecyclerViewAdapter;
import com.tricky_tweaks.go.NavigationIconClickListener;
import com.tricky_tweaks.go.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGatePassFragment extends Fragment {

    private RecyclerView recyclerView;
    private GatePassRecyclerViewAdapter gatePassRecyclerViewAdapter;
    private ArrayList<GatePassData> tempList;
    private ArrayList<GatePassData> list;

    DatabaseReference databaseReference;

    public ShowGatePassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void init(View v) {
        recyclerView = v.findViewById(R.id.fragment_show_gp_rv_passes);
        tempList = new ArrayList<>();
        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("GatePasses");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_gate_pass, container, false);

        init(view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("GatePass");

        setUpToolbar(view);

        setUpRecyclerView();

        popList(new FirebaseCallback() {
            @Override
            public void isExist(boolean value) {
            }

            @Override
            public void getList(ArrayList<GatePassData> listData) {
                list.clear();
                list.addAll(listData);
                gatePassRecyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), " " + listData.size(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void popList(FirebaseCallback firebaseCallback) {




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tempList = new ArrayList<>();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        GatePassData g = d.getValue(GatePassData.class);
                        Log.d("SHOW GATE PASS", g.getGp_from()+"");
                        tempList.add(d.getValue(GatePassData.class));
                        gatePassRecyclerViewAdapter.notifyDataSetChanged();
                    }
                    Log.d("SHOW GATE PASS", tempList.size()+"");
                    firebaseCallback.getList(tempList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.product_grid),
                new LinearInterpolator(),
                getActivity().getDrawable(R.drawable.ic_drawer_close),
                getActivity().getDrawable(R.drawable.ic_drawer_open)
        ));
    }

    private void setUpRecyclerView() {

        gatePassRecyclerViewAdapter = new GatePassRecyclerViewAdapter(getContext(), list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(gatePassRecyclerViewAdapter);
    }

}