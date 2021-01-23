package com.example.fitnesstracker.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment{
    private ImageView profilePic;
    private TextView profileName, profileAge, profileGender, profileWeight, profileHeight,
            profileBmi;


    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseReference;
    private String userID;
    private ProfileViewModel profileViewModel;

    public ProfileFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


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

