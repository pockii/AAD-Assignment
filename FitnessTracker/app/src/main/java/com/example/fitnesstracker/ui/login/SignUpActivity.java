package com.example.fitnesstracker.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.fitnesstracker.ui.userdetails.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, confirmEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmEditText = findViewById(R.id.passwordconfrim);
        signUpButton = findViewById(R.id.register);
        signInTextView = findViewById(R.id.signintv);
        progressDialog = new ProgressDialog(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignUpActivity.this, UserDetails.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //Register back end function to be called inside signUpButton
    private void Register(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String password_confirm = confirmEditText.getText().toString();
        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Enter your email");
            return;
        }
        else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Enter your password");
            return;
        }
        else if(TextUtils.isEmpty(password_confirm)){
            confirmEditText.setError("Confirm your password");
            return;
        }
        else if(!password.equals(password_confirm)){
            confirmEditText.setError("Different password");
            return;
        }
        else if(password.length()<4) {
            passwordEditText.setError("length should be less then 4");
            return;
        }
        else if(!isValidEmail(email)) {
            emailEditText.setError("Invalid Email");
            return;
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Sign Up Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}