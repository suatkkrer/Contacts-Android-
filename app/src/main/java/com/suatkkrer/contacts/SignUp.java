package com.suatkkrer.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;



    TextInputLayout signupName, signupMail, signupPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        signupMail = findViewById(R.id.signupMail);
        signupName = findViewById(R.id.signupName);
        signupPassword = findViewById(R.id.signupPassword);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
    }

    public void Sign(View view) {

        CreateNewAccount();

    }

    public void haveAccount(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginScreen.class);
        startActivity(intent);
    }

    private void CreateNewAccount()
    {
        String email = signupMail.getEditText().getText().toString();
        String password = signupPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(SignUp.this, "Email can not be empty", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(SignUp.this, "Password can not be empty", Toast.LENGTH_SHORT).show();
        }

        else
        {
            progressDialog.setTitle("New Account is Creating");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())

                            {
                                String validUserId= mAuth.getCurrentUser().getUid();
                                reference.child("Users").child(validUserId).setValue("");


                                Intent mainPage = new Intent(SignUp.this,LoginScreen.class);
                                mainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(mainPage);
                                finish();

                                Toast.makeText(SignUp.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SignUp.this, "Error: " + message + "Check your information", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}