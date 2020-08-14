package com.suatkkrer.contacts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentContact extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private List<Contact> firstContact;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String validUser;
    FirebaseAuth mAuthorize;
    String name,phone;
    ArrayList arrayList = new ArrayList();
    ListView listView;


    public FragmentContact() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuthorize = FirebaseAuth.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        firstContact = new ArrayList<>();
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));
//        firstContact.add(new Contact("adsfas","asdfasfd"));

//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1);
//        listView.setAdapter(arrayAdapter);

        myRef.child("Users").child(validUser).child("contacts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String name = "null";
                String phone = "null";

                String value = snapshot.getValue(String.class);
                arrayList.add(value);
                System.out.println(value);
                System.out.println(arrayList.size());
//                arrayAdapter.notifyDataSetChanged();

//                    for (int i = 0; i < arrayList.size(); i++) {
//                        if (i % 2 == 0) {
//                            name = String.valueOf(arrayList.get(i));
//                        } else {
//                            phone = String.valueOf(arrayList.get(i));
//                            firstContact.add(new Contact(name,phone));
//                        }
//                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.contact_fragment,container,false);
//        listView = v.findViewById(R.id.listView);
//        myrecyclerview = v.findViewById(R.id.contact_recyclerview);
//        RecylerViewAdapter recylerViewAdapter = new RecylerViewAdapter(getContext(),firstContact);
//        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        myrecyclerview.setAdapter(recylerViewAdapter);
        return v;
    }
}
