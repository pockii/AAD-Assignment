package com.example.fitnesstracker.ui.userdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fitnesstracker.MainActivity;
import com.example.fitnesstracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
public class UserDetails extends AppCompatActivity {

    private Button submitDetailsButton;
    private EditText nameEditText, weightEditText, heightEditText, ageEditText, genderEditText;

    private DatabaseReference userDatabase;
    private FirebaseAuth firebaseAuth;

    private String userID;
    private String userName;
    private String userWeight;
    private String userHeight;
    private String userAge;
    private String userGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        nameEditText = findViewById(R.id.name);
        weightEditText = findViewById(R.id.weight);
        heightEditText = findViewById(R.id.height);
        ageEditText = findViewById(R.id.age);
        genderEditText = findViewById(R.id.gender);

        submitDetailsButton = findViewById(R.id.submitDetails);


        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        getUserInfo();

        submitDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
                Intent intent = new Intent(UserDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getUserInfo() {
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        userName = map.get("name").toString();
                        nameEditText.setText(userName);
                    }
                    if (map.get("weight") != null) {
                        userWeight = map.get("weight").toString();
                        weightEditText.setText(userWeight);
                    }
                    if (map.get("height") != null) {
                        userHeight = map.get("height").toString();
                        heightEditText.setText(userHeight);
                    }
                    if (map.get("age") != null) {
                        userAge = map.get("age").toString();
                        ageEditText.setText(userAge);
                    }
                    if (map.get("gender") != null) {
                        userGender = map.get("gender").toString();
                        genderEditText.setText(userGender);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void saveUserInformation() {

        userName = nameEditText.getText().toString();
        userWeight = weightEditText.getText().toString();
        userHeight = heightEditText.getText().toString();
        userAge = ageEditText.getText().toString();
        userGender = genderEditText.getText().toString();


        Map userInfo = new HashMap();
        userInfo.put("name", userName);
        userInfo.put("weight", userWeight);
        userInfo.put("height", userHeight);
        userInfo.put("age", userAge);
        userInfo.put("gender", userGender);
        userDatabase.updateChildren(userInfo);

        finish();
    }
}
