package com.suatkkrer.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity implements RecylerViewAdapter.OnNoteListener {

    private FirebaseAuth mAuthorize;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    String validUser;
    RecylerViewAdapter recylerViewAdapter;
    ArrayList<String> userName;
    ArrayList<String> userPhone;
    ArrayList<String> userID;
//    ArrayList<String> userID;
    RecyclerView recyclerView;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        userName = new ArrayList<>();
        userPhone = new ArrayList<>();
        userID = new ArrayList<>();

        mAuthorize = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();
        reference = firebaseDatabase.getReference("Users");
        listView = findViewById(R.id.liste);
        recyclerView = findViewById(R.id.contact_recyclerview);
        getDataFirebase();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recylerViewAdapter = new RecylerViewAdapter(userName,userPhone,this);
        recyclerView.setAdapter(recylerViewAdapter);




//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
//        listView.setAdapter(arrayAdapter);
//        reference.child(validUser).child("contacts").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                String value = snapshot.getValue(String.class);
//                arrayList.add(value);
//                System.out.println(value);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        adapter.AddFragment(new FragmentContact(),"");
//        adapter.AddFragment(new FragmentFav(),"");
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_group);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_star);

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDataFirebase(){

        DatabaseReference reference = firebaseDatabase.getReference("Users");
        reference.child(validUser).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName.clear();
                userPhone.clear();
                userID.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Contact contact = snapshot1.getValue(Contact.class);
                    if (contact.getName() != null && contact.getPhone() != null) {
                        String txt = contact.getName();
                        String txtphone = contact.getPhone();
                        String userid = contact.getId();
                        userName.add(txt);
                        userPhone.add(txtphone);
                        userID.add(userid);
                    }
                }
                recylerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        userName.get(position);
        userPhone.get(position);
        Intent intent = new Intent(getApplicationContext(),EditActivity.class);
        intent.putExtra("name",userName.get(position));
        intent.putExtra("phone",userPhone.get(position));
        intent.putExtra("id",userID.get(position));
        startActivity(intent);
    }
}























