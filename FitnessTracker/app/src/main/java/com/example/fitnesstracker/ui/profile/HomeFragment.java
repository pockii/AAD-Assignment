package com.example.fitnesstracker.ui.profile;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.login.SignInActivity;
import com.example.fitnesstracker.ui.userdetails.UserDetails;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    private ImageView profilePic;
    private TextView profileName, profileAge, profileGender, profileWeight, profileHeight,
            profileBmi, selectWeightPicker, selectHeightPicker;


    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseReference;
    private String userID;



    private HomeViewModel homeViewModel;

    public HomeFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileName = getActivity().findViewById(R.id.displayName);
        profileAge = getActivity().findViewById(R.id.displayAge);
        profileGender = getActivity().findViewById(R.id.displayGender);

        profileWeight = getActivity().findViewById(R.id.displayWeight);
        profileHeight = getActivity().findViewById(R.id.displayHeight);
        profileBmi = getActivity().findViewById(R.id.displayBmi);
        //initialize height and weight select
        selectHeightPicker = getActivity().findViewById(R.id.selectHeight);
        selectWeightPicker = getActivity().findViewById(R.id.selectWeight);


        profilePic = getActivity().findViewById(R.id.uploadImage);

        //Declare firebase Variables
        firebaseAuth = FirebaseAuth.getInstance();
        //not used variable FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        //Getting Data from Firebase
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String age = dataSnapshot.child("age").getValue().toString();
                String weight = dataSnapshot.child("weight").getValue().toString();
                String height = dataSnapshot.child("height").getValue().toString();
                profileName.setText(name);
                profileAge.setText(age);
                profileGender.setText(gender + ",");
                profileWeight.setText(weight + "KG");
                profileHeight.setText(height + "CM");
               }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Take Photo With Camera and upload to firebase

        }


}

