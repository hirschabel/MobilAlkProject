package com.example.mobilalkproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = com.example.mobilalkproject.RegisterActivity.class.getName();
    private static final int SECRET_KEY = 99;

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPassword2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 = findViewById(R.id.editTextPassword2);

        mAuth = FirebaseAuth.getInstance();

    }

    public void onRegister(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordConfirm = editTextPassword2.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "Passwords do not match!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    arukeresoStart();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error in creating user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void arukeresoStart() {
        Intent intent = new Intent(this, ArukeresoActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void onCancel(View view) {
        finish();
    }
}