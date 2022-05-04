package com.example.mobilalkproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangeActivity extends AppCompatActivity {
    private FirebaseUser user;

    EditText editTextOldPassword;
    EditText editTextNewPassword;
    EditText editTextNewPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || user.isAnonymous()) {
            finish();
        }
        editTextOldPassword = findViewById(R.id.editTextOldPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextNewPassword2 = findViewById(R.id.editTextNewPassword2);


    }

    public void onCancelButton(View view) {
        finish();
    }

    public void onSavePassword(View view) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), editTextOldPassword.getText().toString());

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updatePassword();
                    finish();
                } else {
                    Toast.makeText(PasswordChangeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePassword() {
        if (editTextNewPassword.getText().toString().equals(editTextNewPassword2.getText().toString())) {
            user.updatePassword(editTextNewPassword.getText().toString());
        } else {
            Toast.makeText(PasswordChangeActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
    }
}