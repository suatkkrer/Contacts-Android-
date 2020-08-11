package com.suatkkrer.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactList extends AppCompatActivity {

    private FirebaseAuth mAuthorize;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    String validUser;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mAuthorize = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();
        reference = firebaseDatabase.getReference("Users");


        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        linearLayout = findViewById(R.id.add_contact_linear);

        adapter.AddFragment(new FragmentContact(),"");
        adapter.AddFragment(new FragmentFav(),"");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_group);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_star);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.example_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.log_out:
                mAuthorize.signOut();
                finish();
                Intent intent = new Intent(ContactList.this, LoginScreen.class);
                startActivity(intent);
                return true;
            case R.id.add_contact:
                Intent intent1 = new Intent(getApplicationContext(),add.class);
                startActivity(intent1);
                reference.child(validUser).setValue("Aaaaaa");
                Toast.makeText(this, "sdfaaaaaaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}