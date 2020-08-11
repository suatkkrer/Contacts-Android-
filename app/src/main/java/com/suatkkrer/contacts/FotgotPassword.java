package com.suatkkrer.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class FotgotPassword extends AppCompatActivity {

    Button forgotButton;
    TextInputLayout forgotMail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotgot_password);

        forgotButton = findViewById(R.id.forgot_button);
        forgotMail = findViewById(R.id.forgot_email);

        auth = FirebaseAuth.getInstance();



    }

    public void sendPassword(View view) {
        auth.sendPasswordResetEmail(forgotMail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (TextUtils.isEmpty(forgotMail.getEditText().getText().toString())){
                    Toast.makeText(FotgotPassword.this, "Email area can not be empty", Toast.LENGTH_SHORT).show();
                }
                if (task.isSuccessful()){
                    Toast.makeText(FotgotPassword.this, "Password is sent to your email", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FotgotPassword.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}