package com.tricky_tweaks.go.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tricky_tweaks.go.DataModel.GatePassData;
import com.tricky_tweaks.go.R;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_gp_layout, parent, false);

        RecyclerView.ViewHolder holder = new PassViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PassViewHolder p = (PassViewHolder) holder;
            p.textViewFrom.setText(list.get(position).getGp_from());
            p.textViewTo.setText(list.get(position).getGp_to());
            p.textViewModerator.setText(list.get(position).getGp_moderator());
            p.textViewTime.setText(list.get(position).getGp_time());
            p.textViewUID.setText(list.get(position).getS_id());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class PassViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUID;
        TextView textViewTo;
        TextView textViewFrom;
        TextView textViewModerator;
        TextView textViewTime;
        TextView textViewDate;

        public PassViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUID = itemView.findViewById(R.id.card_view_ap_user_id);
            textViewFrom = itemView.findViewById(R.id.card_view_gp_from);
            textViewTo = itemView.findViewById(R.id.card_view_gp_to);
            textViewModerator = itemView.findViewById(R.id.card_view_gp_moderator);
            textViewTime = itemView.findViewById(R.id.card_view_gp_time);
        }
    }
}
