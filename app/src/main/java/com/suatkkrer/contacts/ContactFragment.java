package com.suatkkrer.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactFragment extends Fragment implements RecylerViewAdapter.OnNoteListener{

    RecylerViewAdapter recylerViewAdapter;
    ArrayList<String> userName;
    ArrayList<String> userPhone;
    ArrayList<String> userID;
    TextView contactText;
    //    ArrayList<String> userID;
    HashMap<String, Object> contactNumb = new HashMap<>();
    RecyclerView recyclerView;
    private FirebaseAuth mAuthorize;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    String validUser;
    String id;
    View v;
    Context thisContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;
        thisContext = container.getContext();
        v = inflater.inflate(R.layout.fragment_contact,container,false);
        recyclerView = v.findViewById(R.id.contact_recyclerview);
        contactText = v.findViewById(R.id.contactFragmentText);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recylerViewAdapter = new RecylerViewAdapter(userName,userPhone,this);
        recyclerView.setAdapter(recylerViewAdapter);

        if (recylerViewAdapter.getItemCount() == 0){
            contactText.setVisibility(View.VISIBLE);
        } else {
            contactText.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = new ArrayList<>();
        userPhone = new ArrayList<>();
        userID = new ArrayList<>();


        mAuthorize = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();
        reference = firebaseDatabase.getReference("Users");

        getDataFirebase();





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
        Intent intent = new Intent(thisContext,EditActivity.class);
        intent.putExtra("name",userName.get(position));
        intent.putExtra("phone",userPhone.get(position));
        intent.putExtra("id",userID.get(position));
        startActivity(intent);
    }
}
