package com.example.fitnesstracker.ui.userdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitnesstracker.MainActivity;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.activity.RunInterface;
import com.example.fitnesstracker.ui.login.SignInActivity;
import com.example.fitnesstracker.ui.profile.NumberPickerDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
public class UserDetails extends AppCompatActivity {

    private Button submitDetailsButton;
    private EditText nameEditText, weightEditText, heightEditText, ageEditText, genderEditText;

    DatabaseReference firebaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        //Display Userinfo
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        getUserInfo();

        submitDetailsButton = findViewById(R.id.submitDetails);

        submitDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();//Get the rootnode of database
                firebaseReference = firebaseDatabase.getReference("Users");//Get the path from the rootnode

                //Get all the values
                userName = nameEditText.getText().toString();
                userWeight = weightEditText.getText().toString();
                userHeight = heightEditText.getText().toString();
                userAge = ageEditText.getText().toString();
                userGender = genderEditText.getText().toString();

                UserHelperClass helperClass = new UserHelperClass(userName, userWeight, userHeight, userAge, userGender);

                firebaseReference.child(userID).setValue(helperClass);//set value will store the details e.g name, phone,email address etc
                Toast.makeText(getApplicationContext(), "User Details Saved", Toast.LENGTH_SHORT).show();
                startApplication();
            }
        });

    }
    private void getUserInfo() {
        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") == null) {
                        userName = map.get("name").toString();
                        nameEditText.setText(userName);
                    }
                    if (map.get("weight") == null) {
                        userWeight = map.get("weight").toString();
                        weightEditText.setText(userWeight);
                    }
                    if (map.get("height") == null) {
                        userHeight = map.get("height").toString();
                        heightEditText.setText(userHeight);
                    }
                    if (map.get("age") == null) {
                        userAge = map.get("age").toString();
                        ageEditText.setText(userAge);
                    }
                    if (map.get("gender") == null) {
                        userGender = map.get("gender").toString();
                        genderEditText.setText(userGender);
                    }
                    else{
                        startApplication();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void startApplication() {
        Intent intent = new Intent(UserDetails.this, MainActivity.class);
        startActivity(intent);

    }

}


