package com.suatkkrer.contacts;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    ListView listView;
    View v;
    Context thisContext;
    private FirebaseAuth mAuthorize;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    String validUser;
    String id;
    String deleteId ;
    ArrayList<String> userName;
    ArrayList<String> userPhone;
    ArrayList<String> userID;
    ArrayList<String> userNameDuplicated;
    ArrayList<String> userPhoneDuplicated;
    ArrayList<String> userIdDuplicated;
    ArrayList<String> userNameDuplicated2;
    ArrayList<String> userPhoneDuplicated2;
    ArrayList<String> userIdDuplicated2;
    ArrayList<String> bringName;
    ArrayList<String> bringPhone;
    ArrayList<String> bringId;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_settings,container,false);
        assert container != null;
        thisContext = container.getContext();
        listView = v.findViewById(R.id.settingsList);
        final ArrayList<String> settings = new ArrayList<>();
        settings.add(getString(R.string.importToApp));
        settings.add(getString(R.string.exportToPhoneBook));
        settings.add(getString(R.string.deleteDuplicated));
        settings.add(getString(R.string.DeleteContactFromApp));
        settings.add(getString(R.string.logOut));

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.settingslayout,settings);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     if (position == 0) {


                         if ( ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {



                             AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                             alert.setTitle(R.string.sure);
                             alert.setMessage(R.string.uploadContacts);
                             alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {

                                     final ProgressDialog progressDialog2 = new ProgressDialog(getActivity());

                                     progressDialog2.setTitle(getString(R.string.uploading));
                                     progressDialog2.setMessage(getString(R.string.ContactAreUploading));
                                     progressDialog2.setCanceledOnTouchOutside(true);
                                     progressDialog2.show();

                                     bringContacts();

                                     progressDialog2.dismiss();

                                     Toast toast = Toast.makeText(getActivity(), R.string.contactsUploaded, Toast.LENGTH_LONG);
                                     toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                     toast.show();

                                     ContactFragment contactFragment = new ContactFragment();
                                     FragmentManager manager = getFragmentManager();
                                     manager.beginTransaction()
                                             .replace(R.id.fragment_container,contactFragment,contactFragment.getTag())
                                             .commit();



                                 }
                             });
                             alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     Toast toast = Toast.makeText(getActivity(), R.string.contactsAreNot, Toast.LENGTH_LONG);
                                     toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                     toast.show();
                                 }
                             });
                             alert.create().show();
                         }
                         else {
                                 Snackbar.make(view, R.string.required, Snackbar.LENGTH_INDEFINITE)
                                         .setAction(R.string.givePermiss, new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                                 if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                                                     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 3);
                                                 } else {
                                                     Intent intent = new Intent();
                                                     intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                     Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                                     intent.setData(uri);
                                                     getActivity().startActivity(intent);
                                                 }
                                             }
                                         }).show();
                          }
                     }
                     else if (position == 1){

                         if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                             AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                             alert.setTitle(R.string.sure);
                             alert.setMessage(R.string.exportContacts);
                             alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             importContacts();
                                             Toast toast = Toast.makeText(getActivity(), "Contacts are uploaded to your phone book.", Toast.LENGTH_LONG);
                                             toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                             toast.show();
                                         }
                                     });
                             alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     Toast toast = Toast.makeText(getActivity(), R.string.contactsAreNot, Toast.LENGTH_LONG);
                                     toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                     toast.show();
                                 }
                             });
                             alert.create().show();



                         }
                          else {
                             Snackbar.make(view, R.string.permission,Snackbar.LENGTH_INDEFINITE)
                                     .setAction(R.string.give, new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_CONTACTS)){
                                                 ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.WRITE_CONTACTS},4);
                                             } else {
                                                 Intent intent = new Intent();
                                                 intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                 Uri uri = Uri.fromParts("package",getContext().getPackageName(),null);
                                                 intent.setData(uri);
                                                 getActivity().startActivity(intent);
                                             }
                                         }
                                     }).show();
                         }
                     }  else if (position ==2){
                         AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                         alert.setTitle(R.string.sureAre);
                         alert.setMessage(R.string.duplicatedDelete);
                         alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 getDataFirebase();
                                 Toast toast = Toast.makeText(getActivity(), R.string.duplicatedDeleted, Toast.LENGTH_LONG);
                                 toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                 toast.show();
                             }
                         });
                         alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 Toast toast = Toast.makeText(getActivity(), R.string.deletingProcess, Toast.LENGTH_LONG);
                                 toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                 toast.show();
                             }
                         });
                         alert.create().show();


                     }
                    else if (position == 3) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle(R.string.sureAre);
                        alert.setMessage(R.string.contactWillBe);
                        alert.setPositiveButton(R.string.yess, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearContacts();
                                Toast toast = Toast.makeText(getActivity(), R.string.yourAllContacts, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                toast.show();

                            }
                        });
                        alert.setNegativeButton(R.string.noo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast toast = Toast.makeText(getActivity(), R.string.deletingProcess, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                toast.show();
                            }
                        });
                        alert.create().show();
                        
                     }
                    else if (position == 4) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle(R.string.AreSure);
                        alert.setMessage(R.string.LogOutt);
                        alert.setPositiveButton(R.string.yesss, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuthorize.signOut();
                                getActivity().finish();
                                Intent intent = new Intent(getContext(), LoginScreen.class);
                                startActivity(intent);
                            }
                        });
                        alert.setNegativeButton(R.string.Nooo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast toast = Toast.makeText(getActivity(), R.string.loggingProcess, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                                toast.show();
                            }
                        });
                        alert.create().show();
                     }
            }
        });


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
        bringName = new ArrayList<>();
        bringPhone = new ArrayList<>();
        bringId = new ArrayList<>();


        mAuthorize = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        validUser = mAuthorize.getCurrentUser().getUid();
        reference = firebaseDatabase.getReference("Users");



        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_CONTACTS},1);
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.WRITE_CONTACTS},7);
        }
    }

    private void getDataFirebase() {

        final ProgressDialog progressDialog3 = new ProgressDialog(getActivity());

        progressDialog3.setTitle(getString(R.string.uploading));
        progressDialog3.setMessage(getString(R.string.ContactAreUploading));
        progressDialog3.setCanceledOnTouchOutside(true);
        progressDialog3.show();

        final DatabaseReference referenceDelete = firebaseDatabase.getReference("Users")
                .child(validUser).child("contacts");

        DatabaseReference reference = firebaseDatabase.getReference("Users");
        reference.child(validUser).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName.clear();
                userPhone.clear();
                userID.clear();


                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
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

                for (int i = 0; i < userName.size(); i++) {
                    if (userNameDuplicated2.contains(userName.get(i))) {
                        break;
                    }
                    for (int j = i + 1; j < userName.size(); j++) {
                        if (userName.get(i).equals(userName.get(j)) && !userNameDuplicated2.contains(userName.get(i))) {
                            userNameDuplicated2.add(userName.get(i));
                            userPhoneDuplicated2.add(userPhone.get(i));
                            userIdDuplicated2.add(userID.get(i));
                            deleteId = userID.get(i);
                            referenceDelete.child(deleteId).removeValue();
                        }

                        if (userName.get(i).equals(userName.get(j))) {
                            userNameDuplicated2.add(userName.get(j));
                            userPhoneDuplicated2.add(userPhone.get(j));
                            userIdDuplicated2.add(userID.get(j));
                            deleteId = userID.get(j);
                            referenceDelete.child(deleteId).removeValue();

                        }
                    }
                }
                progressDialog3.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void bringContacts(){

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {



            Toast toast = Toast.makeText(getActivity(), R.string.PleaseWait, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
            toast.show();

            String name = null;
            String number = null;

            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor phone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

            String column = ContactsContract.Contacts.DISPLAY_NAME;
            String col = ContactsContract.CommonDataKinds.Phone.NUMBER;



            while (phone.moveToNext()) {

                name = phone.getString(phone.getColumnIndex(column));
                number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if (name != null && number != null) {
                    id = reference.push().getKey();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(validUser).child("contacts").child(id);
                    Contact contact = new Contact(id, name, number);

                    databaseReference.setValue(contact);
                }
            }
            phone.close();
        } else {
            Toast toast = Toast.makeText(getActivity(), R.string.PleaseGivePermission, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
            toast.show();
        }
    }

    public void bringContacts2(){

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

            Toast toast = Toast.makeText(getActivity(), R.string.PleaseWait, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
            toast.show();

            String name = null;
            String number = null;

            Cursor phone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

            String column = ContactsContract.Contacts.DISPLAY_NAME;

            while (phone.moveToNext()) {

                name = phone.getString(phone.getColumnIndex(column));
                number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                bringName.add(name);
                bringPhone.add(number);

            }
            System.out.println(bringName);
            System.out.println(bringPhone);
            phone.close();

            for (int i = 0; i < bringName.size() ; i++) {

     //           if (name != null && number != null) {
                    id = reference.push().getKey();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(validUser).child("contacts").child(id);
                    Contact contact = new Contact(id, bringName.get(i), bringPhone.get(i));

                    databaseReference.setValue(contact);
         //       }

            }

        } else {
            Toast toast = Toast.makeText(getActivity(), R.string.PleaseGivePermission, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
            toast.show();
        }
    }


    public void clearContacts(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(validUser).child("contacts");
        databaseReference.removeValue();
    }

    public void importContacts(){

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());

        progressDialog.setTitle(getString(R.string.uploading));
        progressDialog.setMessage(getString(R.string.ContactAreUploading));
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        DatabaseReference reference = firebaseDatabase.getReference("Users");
        reference.child(validUser).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                userName.clear();
                userPhone.clear();
                userID.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Contact contact = snapshot1.getValue(Contact.class);

                    if (contact.getName() != null && contact.getPhone() != null) {

                        String txt = contact.getName();
                        String txtphone = contact.getPhone();
                        String userid = contact.getId();
                        userName.add(txt);
                        userPhone.add(txtphone);
                        userID.add(userid);

                        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                        ops.add(ContentProviderOperation.newInsert(
                                ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build());


                        if (txt != null) {
                            ops.add(ContentProviderOperation.newInsert(
                                    ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(
                                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                            txt).build());

                        }

                        if (txtphone != null) {
                            ops.add(ContentProviderOperation.
                                    newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, txtphone)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                    .build());
                        }

                        try {
                            Context appContext = ContactList.getContextOfApplication();
                            appContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void importContacts2(){

//        progressDialog = new ProgressDialog(getActivity());
//
//
//        progressDialog.setTitle(getString(R.string.uploading));
//        progressDialog.setMessage(getString(R.string.ContactAreUploading));
//        progressDialog.setCanceledOnTouchOutside(true);
//        progressDialog.show();


        DatabaseReference reference = firebaseDatabase.getReference("Users");
        reference.child(validUser).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                userName.clear();
                userPhone.clear();
                userID.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
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



                        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                        ops.add(ContentProviderOperation.newInsert(
                                ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build());


                        if (userName.size() != 0) {
                            for (int i = 0; i < userName.size() ; i++) {

                                ops.add(ContentProviderOperation.newInsert(
                                        ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                        .withValue(ContactsContract.Data.MIMETYPE,
                                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                        .withValue(
                                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                                userName.get(i)).build());
                            }
                        }

                        if (userPhone.size() != 0) {
                            for (int i = 0; i < userPhone.size() ; i++) {

                                ops.add(ContentProviderOperation.
                                        newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                        .withValue(ContactsContract.Data.MIMETYPE,
                                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, userPhone.get(i))
                                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                        .build());
                            }
                        }
                        try {
                            Context appContext = ContactList.getContextOfApplication();
                            appContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
 //                           progressDialog.dismiss();
//                                                 Toast toast = Toast.makeText(getActivity(), "Contacts uploaded to your phone book.", Toast.LENGTH_LONG);
//                                                 toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
//                                                 toast.show();
                        } catch (Exception e) {
                            e.printStackTrace();
   //                         progressDialog.dismiss();
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    }

