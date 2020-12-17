package com.suatkkrer.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    TextInputLayout name_id,phone_id;
    private FirebaseAuth mAuthorize;
    String validUser;
    String id, name,phone,name2,phone2,id2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name_id = findViewById(R.id.dialog_name_id);
        phone_id = findViewById(R.id.dialog_phone_id);
        mAuthorize = FirebaseAuth.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        id = intent.getStringExtra("id");
        name_id.getEditText().setText(name);
        phone_id.getEditText().setText(phone);



    }

    public void deleteContact(View view) {
        Intent intent;



    //    if (name != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(validUser).child("contacts").child(id);
            reference.removeValue();
            intent = new Intent(getApplicationContext(), ContactList.class);
            Toast toast = Toast.makeText(getApplicationContext(), R.string.contactDeleted, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
            toast.show();
            startActivity(intent);
//        } else {
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container,new DuplicateFragment()).addToBackStack(ContactList.class.getSimpleName()).commit();
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
//                    .child(validUser).child("contacts").child(id2);
//            reference.removeValue();
//        }

    }

    public void updateData(View view) {

        if (TextUtils.isEmpty(name_id.getEditText().getText().toString()) || TextUtils.isEmpty(phone_id.getEditText().getText().toString()))
        {
            Toast.makeText(this, R.string.enterNamePhoneNumber, Toast.LENGTH_SHORT).show();
        } else
        {
        //    if (name != null){
                update(id,name_id.getEditText().getText().toString(),phone_id.getEditText().getText().toString());
                Intent intent = new Intent(getApplicationContext(),ContactList.class);
                Toast.makeText(this, R.string.contactUpdated, Toast.LENGTH_SHORT).show();
                startActivity(intent);
//            } else {
//                update(id2,name_id.getEditText().getText().toString(),phone_id.getEditText().getText().toString());
//                Toast.makeText(this, R.string.contactUpdated, Toast.LENGTH_SHORT).show();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container,new DuplicateFragment()).addToBackStack(ContactList.class.getSimpleName()).commit();
//            }

        }

    }
    public boolean update(String id,String name, String phone){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(validUser).child("contacts").child(id);

        Contact contact = new Contact(id,name,phone);

        databaseReference.setValue(contact);

        return true;
    }

    public void hidee(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}