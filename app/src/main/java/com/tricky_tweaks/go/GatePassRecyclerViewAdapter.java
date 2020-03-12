package com.tricky_tweaks.go;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GatePassRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    ArrayList<GatePassData> list;

    public GatePassRecyclerViewAdapter(Context context, ArrayList<GatePassData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        RecyclerView.ViewHolder holder = new PassViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PassViewHolder p = (PassViewHolder) holder;
            p.tv.setText(list.get(position).getGp_from()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class PassViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public PassViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text_view_card_View);
        }
    }
}
