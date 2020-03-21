package com.tricky_tweaks.go.Adapter;

import android.content.Context;
import android.graphics.PostProcessor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students");
            ref.child(FirebaseAuth.getInstance().getUid()).child("s_name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    p.textViewName.setText("name : "+ dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            if (list.get(position).getGp_status() == 1) {
                p.requestButton.setChipIcon(context.getDrawable(R.drawable.fui_ic_check_circle_black_128dp));
                p.textViewstatus.setText("accepted");
                p.textViewstatus.setTextColor(context.getColor(R.color.green));
            }
            else if (list.get(position).getGp_status() == 0) {
                p.requestButton.setChipIcon(context.getDrawable(R.drawable.ic_cancel_24));
                p.textViewstatus.setText("denied");
                p.textViewstatus.setTextColor(context.getColor(R.color.pureRed));
            } else {
                p.requestButton.setChipIcon(context.getDrawable(R.drawable.ic_question));
                p.textViewstatus.setText("pending");
                p.textViewstatus.setTextColor(context.getColor(R.color.ceriseRed));
            }

            p.request();

            p.requestButton.setOnClickListener(n -> {
                showAlertDialog(p.requestButton, position);
                Toast.makeText(context, "CLICKED", Toast.LENGTH_SHORT).show();
            });


    }

    public void showAlertDialog(Chip button, int position) {
        new MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                .setTitle("Request")
                .setMessage("Would you like to accept the request ")
                .setCancelable(false)
                .setPositiveButton("Accept", (dialog, which) -> {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GatePass");
                    reference.child(list.get(position).getGp_id()).child("gp_status").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                button.setChipIcon(context.getDrawable(R.drawable.fui_ic_check_circle_black_128dp));
                                Toast.makeText(context, "successfull", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            button.setChipIcon(context.getDrawable(R.drawable.ic_question));
                            Toast.makeText(context, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }).setNeutralButton("Denied", (dialog, which) -> {
            dialog.dismiss();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GatePass");
            reference.child(list.get(position).getGp_id()).child("gp_status").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        button.setChipIcon(context.getDrawable(R.drawable.ic_cancel_24));
                        Toast.makeText(context, "successfull", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(e -> {
                button.setChipIcon(context.getDrawable(R.drawable.ic_question));
                Toast.makeText(context, "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        }).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class PassViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUID;
        TextView textViewName;
        TextView textViewTo;
        TextView textViewFrom;
        TextView textViewModerator;
        TextView textViewTime;
        TextView textViewDate;
        TextView textViewstatus;
        Chip requestButton;
        LottieAnimationView thumbUpLottie;
        LottieAnimationView thumbDownLottie;

        boolean doubleTap = false;

        public PassViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUID = itemView.findViewById(R.id.card_view_ap_user_id);
            textViewFrom = itemView.findViewById(R.id.card_view_gp_from);
            textViewTo = itemView.findViewById(R.id.card_view_gp_to);
            textViewModerator = itemView.findViewById(R.id.card_view_gp_moderator);
            textViewTime = itemView.findViewById(R.id.card_view_gp_time);
        
            textViewstatus = itemView.findViewById(R.id.card_view_gp_status);
            textViewName = itemView.findViewById(R.id.card_view_gp_user_name);
            requestButton = itemView.findViewById(R.id.card_view_gp_chip_request);

            thumbUpLottie = itemView.findViewById(R.id.card_view_gp_thumb_up);
            thumbDownLottie = itemView.findViewById(R.id.card_view_gp_thumb_down);
        }

        public void request() {
            thumbUpLottie.setOnClickListener(v -> {
                if (!doubleTap) {
                    doubleTap = true;
                    new Handler().postDelayed(() -> {
                        doubleTap = false;
                    }, 2000);
                    return;
                }

                Toast.makeText(context, "double tapped ", Toast.LENGTH_SHORT).show();

                thumbDownLottie.setProgress(0);
                thumbDownLottie.cancelAnimation();

                thumbUpLottie.setProgress(0);
                thumbUpLottie.pauseAnimation();
                thumbUpLottie.playAnimation();
            });

            thumbDownLottie.setOnClickListener(v -> {

                thumbUpLottie.setProgress(0);
                thumbUpLottie.cancelAnimation();

                thumbDownLottie.setProgress(0);
                thumbDownLottie.pauseAnimation();
                thumbDownLottie.playAnimation();
            });
        }

        public void acceptRequest() {

        }

        public void rejectRequest() {

        }

    }
}
