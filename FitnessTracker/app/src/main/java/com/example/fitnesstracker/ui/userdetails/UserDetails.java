package com.example.fitnesstracker.ui.userdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fitnesstracker.MainActivity;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.login.SignInActivity;
import com.example.fitnesstracker.ui.login.SignUpActivity;

public class UserDetails extends AppCompatActivity {
    private Button submitDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        submitDetailsButton = findViewById(R.id.submitDetails);

        submitDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



}