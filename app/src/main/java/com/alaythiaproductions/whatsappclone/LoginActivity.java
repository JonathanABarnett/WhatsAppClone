package com.alaythiaproductions.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private Button loginButton, phoneLoginButton;
    private EditText userEmail, userPassword;
    private TextView needNewAccountLink, forgotPasswordLink;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        initializeFields();

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowUserToLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null) {
            sendUserToMainActivity();
        }
    }

    private void allowUserToLogin() {
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

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendUserToMainActivity();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void sendUserToMainActivity() {
        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }

    private void initializeFields() {
        loginButton = findViewById(R.id.login_button);
        phoneLoginButton = findViewById(R.id.login_phone_button);
        userEmail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);
        needNewAccountLink = findViewById(R.id.login_sign_up);
        forgotPasswordLink = findViewById(R.id.login_forgot_password);
        progressBar  = findViewById(R.id.login_progress);
        progressBar.setVisibility(View.GONE);
    }
}
