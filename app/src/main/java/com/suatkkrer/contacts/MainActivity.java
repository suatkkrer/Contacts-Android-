package com.suatkkrer.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    Animation topAnim, bottomAnim;
    ImageView phoneImage;
    TextView phoneText,secureText;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        phoneImage = findViewById(R.id.phoneImage);
        phoneText = findViewById(R.id.phoneText);
        secureText = findViewById(R.id.secureText);

        phoneImage.setAnimation(topAnim);
        phoneText.setAnimation(bottomAnim);
        secureText.setAnimation(bottomAnim);

        if (firebaseUser != null){
            Intent intent = new Intent(MainActivity.this,ContactList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(phoneImage, "logo_image");
                    pairs[1] = new Pair<View, String>(phoneText, "logo_name");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            }, SPLASH_SCREEN);
        }
    }
}