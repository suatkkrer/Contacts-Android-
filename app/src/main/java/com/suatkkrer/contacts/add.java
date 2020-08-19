package com.suatkkrer.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class add extends AppCompatActivity {

    TextInputLayout contactAdd,numberAdd;

    FirebaseAuth mAuth;
    String validUser,id;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    HashMap<String, Object> contactNumb = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        contactAdd = findViewById(R.id.contact_add);
        numberAdd = findViewById(R.id.phone_add);


        mAuth = FirebaseAuth.getInstance();
        validUser = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users");

    }

    public void addContact(View view) {

        if (TextUtils.isEmpty(contactAdd.getEditText().getText().toString()) || TextUtils.isEmpty(numberAdd.getEditText().getText().toString())){
            Toast.makeText(this, "Name field or number field can not be empty", Toast.LENGTH_LONG).show();
        } else {


            id = reference.push().getKey();
//            Contact contact = new Contact(contactAdd.getEditText().getText().toString(),numberAdd.getEditText().getText().toString());
//            arrayList.add(contactAdd.getEditText().getText().toString());
//            arrayList.add(numberAdd.getEditText().getText().toString());
            contactNumb.put("name",contactAdd.getEditText().getText().toString());
            contactNumb.put("phone",numberAdd.getEditText().getText().toString());
            contactNumb.put("id",id);
//            contactNumb.put("id",id);
            reference.child(validUser).child("contacts").child(id).setValue(contactNumb);
            Toast.makeText(this, "You have added your contact", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(add.this,ContactList.class);
            startActivity(intent);
        }
    }

    public void cancel(View view) {
        Intent intent = new Intent(getApplicationContext(),ContactList.class);
        startActivity(intent);
    }
}