package com.tricky_tweaks.go.LoginBundle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.tricky_tweaks.go.R;

public class WelcomeActivity extends AppCompatActivity {

    private ImageButton closeBtn;
    private ImageButton nextBtn;

    private void init_fields() {
        closeBtn = findViewById(R.id.activity_welcome_ib_close);
        nextBtn = findViewById(R.id.activity_welcome_ib_next);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init_fields();

        closeBtn.setOnClickListener(v -> finish());
        nextBtn.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, EntryActivity.class).putExtra("EVENT", 0));
            finish();
        });

    }
}
