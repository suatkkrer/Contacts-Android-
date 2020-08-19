package com.suatkkrer.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactList extends AppCompatActivity {

    private FirebaseAuth mAuthorize;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    String validUser;
    RecylerViewAdapter recylerViewAdapter;
    ArrayList<String> userName;
    ArrayList<String> userPhone;
    ArrayList<String> userID;
//    ArrayList<String> userID;
    HashMap<String, Object> contactNumb = new HashMap<>();
    RecyclerView recyclerView;
    String id;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);



        mAuthorize = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();
        reference = firebaseDatabase.getReference("Users");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ContactFragment()).commit();


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

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.contactsNav:
                            selectedFragment = new ContactFragment();
                            break;
                        case R.id.duplicateNav:
                            selectedFragment = new DuplicateFragment();
                            break;
                        case R.id.settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

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
            case R.id.clearContacts:
                clearContacts();
                return true;
            case R.id.importContacts:
                bringContacts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void bringContacts(){

        if (ContextCompat.checkSelfPermission(ContactList.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            String name = null;
            String number = null;

            ContentResolver contentResolver = getContentResolver();
            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

            ArrayList<String> contactList = new ArrayList<>();
            ArrayList<String> contactList1 = new ArrayList<>();
            String column = ContactsContract.Contacts.DISPLAY_NAME;
            String col = ContactsContract.CommonDataKinds.Phone.NUMBER;

            while (phone.moveToNext()) {

                name = phone.getString(phone.getColumnIndex(column));
                number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if (name != null && number != null) {
//                contactNumb.clear();
                    id = reference.push().getKey();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(validUser).child("contacts").child(id);
//                contactNumb.put("name",name);
//                contactNumb.put("phone",phone);
//                contactNumb.put("id",id);

                    Contact contact = new Contact(id, name, number);

                    databaseReference.setValue(contact);
                }
            }
            phone.close();
        } else {
            Toast.makeText(this, "Please Give Permission for importing contacts", Toast.LENGTH_SHORT).show();
        }
    }
    public void clearContacts(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(validUser).child("contacts");
        databaseReference.removeValue();
    }
}























