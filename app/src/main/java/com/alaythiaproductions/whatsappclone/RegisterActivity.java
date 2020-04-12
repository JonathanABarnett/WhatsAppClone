package com.alaythiaproductions.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText userEmail, userPassword;
    private TextView alreadyHaveAccountLink;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeFields();

        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();

                progressBar.setVisibility(View.GONE);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                createNewAccount();
            }
        });

    }

    private void initializeFields() {
        registerButton = findViewById(R.id.register_button);
        userEmail = findViewById(R.id.register_email);
        userPassword = findViewById(R.id.register_password);
        alreadyHaveAccountLink = findViewById(R.id.register_already_have_account);
        progressBar  = findViewById(R.id.register_progress);
        progressBar.setVisibility(View.GONE);
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void createNewAccount() {
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (email.equals("")) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password must be 6 characters long", Toast.LENGTH_SHORT).show();
        } else {

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendUserToLoginActivity();
                        Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

}
