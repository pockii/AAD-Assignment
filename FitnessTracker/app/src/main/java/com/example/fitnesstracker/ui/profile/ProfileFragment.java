package com.example.fitnesstracker.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesstracker.MainActivity;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.login.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    //Request code for gallery
    private static final int GALLERY_REQUEST = 9;
    //Request code for camera
    private static final int CAMERA_REQUEST = 11;

    //Widgets
    private ImageView profilePic;
    private TextView profileName, profileAge, profileGender, profileWeight, profileHeight,
            profileBmi;
    private Button calculateBmi;
    private Context context;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseReference;
    private String userID;
    private ProfileViewModel profileViewModel;

    public ProfileFragment() {
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
        profileBmi = getActivity().findViewById(R.id.displayBmi);//Answer
        calculateBmi = getActivity().findViewById(R.id.calculateBmi);//Calculate bmi

        profilePic = getActivity().findViewById(R.id.uploadImage);
        context = getContext();

        //Declare firebase Variables
        firebaseAuth = FirebaseAuth.getInstance();
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
                profileWeight.setText(weight);
                profileHeight.setText(height);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        calculateBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getWeight = profileWeight.getText().toString();
                String getHeight = profileHeight.getText().toString();

                float weight = Float.parseFloat(getWeight);
                float height = Float.parseFloat(getHeight);

                float newHeight = height / 100;
                float bmi = weight / (newHeight * newHeight);
                String bmi2 = String.valueOf(Math.round(bmi));
                profileBmi.setText(bmi2);

            }
        });
        //Profile picture on click listener
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageOptionDialog();
            }
        });
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }
    private void capturePictureFromCamera(){
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    private void showImageOptionDialog(){
        final String[] options = getResources().getStringArray(R.array.image_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_image_options)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            case 0:
                                getImageFromGallery();
                                break;
                            case 1:
                                capturePictureFromCamera();
                                break;
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && requestCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            profilePic.setImageURI(selectedImage);
        }
        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data !=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            profilePic.setImageBitmap(bitmap);
        }
    }
}

