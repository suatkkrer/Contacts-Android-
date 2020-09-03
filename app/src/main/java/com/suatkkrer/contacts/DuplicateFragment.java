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

public class DuplicateFragment extends Fragment  implements RecylerViewAdapter.OnNoteListener{


    RecylerViewAdapter recylerViewAdapter;
    ArrayList<String> userName;
    ArrayList<String> userPhone;
    ArrayList<String> userID;
    RecyclerView recyclerView;
    private FirebaseAuth mAuthorize;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> userNameDuplicated;
    ArrayList<String> userPhoneDuplicated;
    ArrayList<String> userIdDuplicated;
    ArrayList<String> userNameDuplicated2;
    ArrayList<String> userPhoneDuplicated2;
    ArrayList<String> userIdDuplicated2;
    TextView duplicatedText;
    String validUser;
    View v;
    Context thisContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;
        thisContext = container.getContext();
        v = inflater.inflate(R.layout.fragment_duplicate,container,false);
        recyclerView = v.findViewById(R.id.duplicate_recyclerview);
        duplicatedText = v.findViewById(R.id.duplicatedFragmentText);

        duplicatedText.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recylerViewAdapter = new RecylerViewAdapter(userNameDuplicated2,userPhoneDuplicated2,  this);
        recyclerView.setAdapter(recylerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName = new ArrayList<>();
        userPhone = new ArrayList<>();
        userID = new ArrayList<>();
        userIdDuplicated = new ArrayList<>();
        userNameDuplicated = new ArrayList<>();
        userPhoneDuplicated = new ArrayList<>();
        userIdDuplicated2 = new ArrayList<>();
        userNameDuplicated2 = new ArrayList<>();
        userPhoneDuplicated2 = new ArrayList<>();


        mAuthorize = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();
        reference = firebaseDatabase.getReference("Users");

        getDataFirebase();
    }

    private void getDataFirebase() {

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

//                for (int i = 0; i < userName.size() ; i++) {
//                    if (!userNameDuplicated.contains(userName.get(i))){
//                        userNameDuplicated.add((userName.get(i)));
//                        userPhoneDuplicated.add(userPhone.get(i));
//                        userIdDuplicated.add(userID.get(i));
//                    } else {
//                        userNameDuplicated2.add(userName.get(i));
//                        userPhoneDuplicated2.add(userPhone.get(i));
//                        userIdDuplicated2.add(userID.get(i));
//                    }
//                }

                for (int i = 0; i < userName.size(); i++) {
                    if (userNameDuplicated2.contains(userName.get(i))){
                        break;
                    }
                    for (int j = i + 1 ; j < userName.size(); j++) {
                        if (userName.get(i).equals(userName.get(j)) && !userNameDuplicated2.contains(userName.get(i))){
                            userNameDuplicated2.add(userName.get(i));
                            userPhoneDuplicated2.add(userPhone.get(i));
                            userIdDuplicated2.add(userID.get(i));
                        }
                        if (userName.get(i).equals(userName.get(j))) {
                            userNameDuplicated2.add(userName.get(j));
                            userPhoneDuplicated2.add(userPhone.get(j));
                            userIdDuplicated2.add(userID.get(j));
                        }
                    }
                }

//                for (int i = 0; i < userPhone.size() ; i++) {
//                    if (!userPhoneDuplicated.contains(userPhone.get(i))){
//                        userPhoneDuplicated.add(userPhone.get(i));
//                        userNameDuplicated.add(userName.get(i));
//                    } else {
//                        userPhoneDuplicated2.add(userPhone.get(i));
//                        userNameDuplicated2.add(userName.get(i));
//                    }
//                }
                recylerViewAdapter.notifyDataSetChanged();
                if (recylerViewAdapter.getItemCount() == 0){
                    duplicatedText.setVisibility(View.VISIBLE);
                } else {
                    duplicatedText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onNoteClick(int position) {

        Intent intent = new Intent(thisContext,EditActivity.class);
        intent.putExtra("nameDuplicated",userNameDuplicated2.get(position));
        intent.putExtra("phoneDuplicated",userPhoneDuplicated2.get(position));
        intent.putExtra("idDuplicated",userIdDuplicated2.get(position));
        startActivity(intent);
    }
}
