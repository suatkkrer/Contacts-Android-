package com.suatkkrer.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginScreen extends AppCompatActivity {

    TextInputLayout username,password;
    ImageView image;
    TextView logo_name,slogan_name;
    Button forgetpassword,login,newuser;
    FirebaseAuth mAuth;
    ProgressDialog loginDialog;
    FirebaseUser presentuser;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        image = findViewById(R.id.logo_image);
        logo_name = findViewById(R.id.logo_name);
        slogan_name = findViewById(R.id.slogan_name);
        forgetpassword = findViewById(R.id.forgetpassword);
        login = findViewById(R.id.login);
        newuser = findViewById(R.id.newuser);

        mAuth = FirebaseAuth.getInstance();
        presentuser = mAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference();
        loginDialog = new ProgressDialog(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_CONTACTS},1);
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_CONTACTS},7);
        }
    }



    public void SignUp(View view) {

        Intent intent = new Intent(LoginScreen.this,SignUp.class);

        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View,String> (image,"logo_image");
        pairs[1] = new Pair<View,String> (logo_name,"logo_name");
        pairs[2] = new Pair<View,String> (slogan_name,"logo_desc");
        pairs[3] = new Pair<View,String> (username,"logo_username");
        pairs[4] = new Pair<View,String> (password,"logo_password");
        pairs[5] = new Pair<View,String> (login,"logo_login");
        pairs[6] = new Pair<View,String> (newuser,"logo_newuser");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginScreen.this,pairs);
        startActivity(intent,options.toBundle());

    }

    private void AuthorizetoLogin()

    {
        String email= username.getEditText().getText().toString();
        String passworda= password.getEditText().getText().toString();

        if(TextUtils.isEmpty(email))

        {
            Toast.makeText(LoginScreen.this, R.string.emailCannotBeEmpty, Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(passworda))

        {
            Toast.makeText(LoginScreen.this, R.string.passwordCannotBeEmpty, Toast.LENGTH_SHORT).show();

        }
        else
        {

            //Progress
            loginDialog.setTitle(getString(R.string.logging));
            loginDialog.setMessage(getString(R.string.pleaseWait));
            loginDialog.setCanceledOnTouchOutside(true);
            loginDialog.show();

            mAuth.signInWithEmailAndPassword(email,passworda)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Intent mainPage = new Intent(LoginScreen.this,ContactList.class);
                                mainPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainPage);
                                finish();
                                Toast.makeText(LoginScreen.this, R.string.loginSuccessful, Toast.LENGTH_SHORT).show();
                                loginDialog.dismiss();

                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(LoginScreen.this, R.string.pleaseCheckPassword, Toast.LENGTH_SHORT).show();
                                loginDialog.dismiss();
                            }

                        }
                    });

        }
    }

    public void forgetPassword(View view) {
        Intent intent = new Intent(getApplicationContext(),FotgotPassword.class);
        startActivity(intent);
    }

    public void login(View view) {
        AuthorizetoLogin();
    }

    public void hide(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}