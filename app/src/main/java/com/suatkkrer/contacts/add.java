package com.suatkkrer.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add extends AppCompatActivity {

    TextInputLayout contactAdd,numberAdd;

    FirebaseAuth mAuth;
    String validUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

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
            Contact contact = new Contact(contactAdd.getEditText().getText().toString(),numberAdd.getEditText().getText().toString());
            reference.child(validUser).child("contacts").setValue(contact);
            Toast.makeText(this, "You have added your contact", Toast.LENGTH_LONG).show();
        }



    }

    public void cancel(View view) {
        Intent intent = new Intent(getApplicationContext(),ContactList.class);
        startActivity(intent);
    }
}