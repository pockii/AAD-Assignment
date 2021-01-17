package com.example.fitnesstracker.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstracker.MainActivity;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.ui.home.HomeFragment;
import com.example.fitnesstracker.ui.userdetails.UserDetails;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    private TextView signUpTextView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signInButton = findViewById(R.id.login);
        signUpTextView = findViewById(R.id.signuptv);
        progressDialog = new ProgressDialog(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void Login(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Enter your email");
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            passwordEditText.setError("Enter your password");
            return;
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SignInActivity.this, UserDetails.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(SignInActivity.this,"Sign Up Failed",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }
    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}