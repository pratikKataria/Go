package com.tricky_tweaks.go.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tricky_tweaks.go.DataModel.GatePassData;
import com.tricky_tweaks.go.R;

import java.util.ArrayList;

public class GatePassRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int CARD_VIEW = 1;
    private static final int EMPTY_VIEW = 0;

    Context context;
    ArrayList<GatePassData> list;

    public GatePassRecyclerViewAdapter(Context context, ArrayList<GatePassData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        if (viewType == CARD_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_gp_layout, parent, false);
            holder = new PassViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
            holder = new EmptyViewLoader(view);
        }
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_gp_layout, parent, false);
//
//        RecyclerView.ViewHolder holder = new PassViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PassViewHolder) {

            PassViewHolder p = (PassViewHolder) holder;
            String from = list.get(position).getGp_from();
            String to = list.get(position).getGp_to();
            String moderator = list.get(position).getGp_moderator();
            String time = list.get(position).getGp_time();
            String id = list.get(position).getS_id();
            String reason = list.get(position).getGp_reason();


            p.setValue(from, to, moderator, time, id, reason, position);


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students");
            ref.child(id).child("s_name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    p.textViewName.setText("Name : " + dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            p.onReasonClicked();
        } else {
            EmptyViewLoader emptyViewLoader = (EmptyViewLoader) holder;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() > 0) {
            return CARD_VIEW;

        } else {
            return EMPTY_VIEW;
        }
    }

    @Override
    public int getItemCount() {

        if (list.size() == 0) {
            return 1;
        } else {
            return list.size();
        }
    }


    public class PassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewUID;
        TextView textViewName;
        TextView textViewTo;
        TextView textViewFrom;
        TextView textViewModerator;
        TextView textViewTime;
        TextView textViewStatus;
        TextView textViewReason;
        TextView textViewReasonBtn;
        TextView textViewDoubleTap;
        CardView cardView;

        LottieAnimationView thumbUpLottie;
        LottieAnimationView thumbDownLottie;

        View mItemView;

        SharedPreferences preferences = context.getSharedPreferences("AppSettingPrefs", 0);
        boolean isAdmin = preferences.getBoolean("IS_ADMIN", false);

        boolean doubleTap = false;
        boolean isHide = true;

        int position = -1;

        public PassViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemView = itemView;

            textViewUID = itemView.findViewById(R.id.card_view_ap_user_id);
            textViewFrom = itemView.findViewById(R.id.card_view_gp_from);
            textViewTo = itemView.findViewById(R.id.card_view_gp_to);
            textViewModerator = itemView.findViewById(R.id.card_view_gp_moderator);
            textViewTime = itemView.findViewById(R.id.card_view_gp_time);
        
            textViewStatus = itemView.findViewById(R.id.card_view_gp_status);
            textViewName = itemView.findViewById(R.id.card_view_gp_user_name);

            textViewReason = itemView.findViewById(R.id.card_view_gp_tv_reason);
            textViewReasonBtn = itemView.findViewById(R.id.card_view_gp_tv_reason_button);

            textViewDoubleTap = itemView.findViewById(R.id.card_view_gp_tv_double_tap);

            thumbUpLottie = itemView.findViewById(R.id.card_view_gp_thumb_up);
            thumbDownLottie = itemView.findViewById(R.id.card_view_gp_thumb_down);
            cardView = itemView.findViewById(R.id.card_view_gp);

            if (!isAdmin) {
                textViewDoubleTap.setVisibility(View.VISIBLE);
                thumbUpLottie.setOnClickListener(this::onClick);
                thumbDownLottie.setOnClickListener(this::onClick);
            }

        }


        void onReasonClicked() {
            textViewReasonBtn.setOnClickListener(n -> {
                if (isHide) {
                    revealReason();
                    isHide = false;
                }else {
                    hideReason();
                    isHide = true;
                }
            });
        }

        void hideReason() {
            textViewReason.setAlpha(1f);

            textViewReason.animate().alpha(0f).setDuration(1000).setListener(null);
            textViewReasonBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_expand_more, 0);
            textViewReason.setVisibility(View.GONE);
        }

        void revealReason() {
            textViewReason.setAlpha(0f);
            textViewReason.setVisibility(View.VISIBLE);

            textViewReason.animate().alpha(1f).setDuration(1000).setListener(null);
            textViewReasonBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_expand_less, 0);
        }

        public void acceptRequest(int position) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GatePass");
            reference.child(list.get(position).getGp_id()).child("gp_status").setValue(1).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "request accepted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "failed to accept request", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(context, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        public void rejectRequest(int position) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GatePass");
            reference.child(list.get(position).getGp_id()).child("gp_status").setValue(0).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "request rejected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }

        public void setValue(String from, String to, String moderator, String time, String id, String reason, int position) {
            textViewFrom.setText(from);
            textViewTo.setText(to);
            textViewModerator.setText(moderator);
            textViewTime.setText(time);
            textViewUID.setText(id);
            textViewReason.setText(reason);

            if (list.get(position).getGp_status() == 1) {
                thumbUpLottie.setProgress(100);
                thumbDownLottie.setProgress(0);
                textViewStatus.setText("accepted");
                textViewStatus.setTextColor(context.getColor(R.color.green));
            } else if (list.get(position).getGp_status() == 0) {
                thumbDownLottie.setProgress(100);
                thumbUpLottie.setProgress(0);
                textViewStatus.setText("denied");
                textViewStatus.setTextColor(context.getColor(R.color.pureRed));
            } else {
                thumbUpLottie.setProgress(0);
                thumbDownLottie.setProgress(0);
                textViewStatus.setText("pending");
                textViewStatus.setTextColor(context.getColor(R.color.ceriseRed));
            }

            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.card_view_gp_thumb_up:
                    if (!doubleTap) {
                        doubleTap = true;
                        new Handler().postDelayed(() -> {
                            doubleTap = false;
                        }, 2000);
                        return;
                    }

                    acceptRequest(position);

                    thumbDownLottie.setProgress(0);
                    thumbDownLottie.cancelAnimation();

                    thumbUpLottie.pauseAnimation();
                    thumbUpLottie.playAnimation();
                    break;
                case R.id.card_view_gp_thumb_down:
                    rejectRequest(position);

                    thumbUpLottie.setProgress(0);
                    thumbUpLottie.cancelAnimation();

                    thumbDownLottie.pauseAnimation();
                    thumbDownLottie.playAnimation();
                    break;
            }
        }
    }

    private class EmptyViewLoader extends RecyclerView.ViewHolder {
        public EmptyViewLoader(View view) {
            super(view);
        }
    }
}
