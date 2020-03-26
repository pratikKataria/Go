package com.tricky_tweaks.go.MainBundle.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tricky_tweaks.go.Adapter.GatePassRecyclerViewAdapter;
import com.tricky_tweaks.go.DataModel.GatePassData;
import com.tricky_tweaks.go.FirebaseCallback;
import com.tricky_tweaks.go.LoginBundle.activity.EntryActivity;
import com.tricky_tweaks.go.NavigationIconClickListener;
import com.tricky_tweaks.go.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGatePassFragment extends Fragment {

    private RecyclerView recyclerView;
    private GatePassRecyclerViewAdapter gatePassRecyclerViewAdapter;
    private ArrayList<GatePassData> tempList;
    private ArrayList<GatePassData> list;

    private TextView textViewLogout;
    private SwipeRefreshLayout refreshLayout;

    private Toolbar toolbar;

    DatabaseReference databaseReference;

    boolean isAdmin = false;

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

        textViewLogout = v.findViewById(R.id.bac_drop_layout_tv_logout);
        refreshLayout = v.findViewById(R.id.swipe_container);

        databaseReference = FirebaseDatabase.getInstance().getReference("GatePass");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_gate_pass, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("AppSettingPrefs", 0);

        isAdmin = preferences.getBoolean("IS_ADMIN", false);

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

        textViewLogout.setOnClickListener(n ->
                signout()
        );

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
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
        });

        return view;
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignIn.getClient(getContext(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();

        SharedPreferences preferences = getActivity().getSharedPreferences("AppSettingPrefs", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IS_ADMIN", false);
        editor.apply();

        Log.e("Show Gate Pass Fragment ", "logout");

        startActivity(new Intent(getContext(), EntryActivity.class).putExtra("EVENT", 0));
        getActivity().finish();
    }

    private void popList(FirebaseCallback firebaseCallback) {

        Query q =  databaseReference.orderByChild("sys_nano").limitToLast(5);


        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("Show Gate Pass Fragment", dataSnapshot.toString());

                if (dataSnapshot.exists()) {
                    tempList = new ArrayList<>();
                    refreshLayout.setRefreshing(false);
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        GatePassData g = d.getValue(GatePassData.class);
                        if (g != null)
                        Log.d("SHOW GATE PASS", g.getGp_from()+"");
                        if (!isAdmin && g.getS_id().equals(FirebaseAuth.getInstance().getUid())) {
                            tempList.add(0, d.getValue(GatePassData.class));
                            gatePassRecyclerViewAdapter.notifyDataSetChanged();
                        }else {
                            tempList.add(0, d.getValue(GatePassData.class));
                            gatePassRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                    Log.d("SHOW GATE PASS", tempList.size()+"");
                    firebaseCallback.getList(tempList);
                    refreshLayout.setRefreshing(false);
                    gatePassRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpToolbar(View view) {
        toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        NavigationIconClickListener iconClickListener = new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.fragment_show_gp_rv_passes_ll_slide),
                new LinearInterpolator(),
                getActivity().getDrawable(R.drawable.ic_drawer_close),
                getActivity().getDrawable(R.drawable.ic_drawer_open)
        );

        toolbar.setNavigationOnClickListener(iconClickListener);

    }

    private void setUpRecyclerView() {

        gatePassRecyclerViewAdapter = new GatePassRecyclerViewAdapter(getContext(), list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(gatePassRecyclerViewAdapter);
    }

}
