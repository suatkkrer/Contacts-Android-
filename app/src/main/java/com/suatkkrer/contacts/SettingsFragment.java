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
import java.util.HashSet;
import java.util.Set;

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
    ArrayList<String> phoneDup;
    ArrayList<String> nameDup;
    int progress = 0;
    int progress4 = 0;
    int progress5 = 0;


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
       // settings.add(getString(R.string.deleteDuplicated));
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

//                                     ExampleThread thread = new ExampleThread();
//                                     thread.start();
                                     bringContacts2();

//                             Toast toast = Toast.makeText(getActivity(), R.string.contactsUploaded, Toast.LENGTH_LONG);
//                             toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
//                             toast.show();

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
                     }
                    /* else if (position ==2){
                         AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                         alert.setTitle(R.string.sureAre);
                         alert.setMessage(R.string.duplicatedDelete);
                         alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 getDataFirebase();

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
                     }*/
                    else if (position == 2) {
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
                    else if (position == 3) {
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
        nameDup = new ArrayList<>();
        phoneDup = new ArrayList<>();


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

        progressDialog3.setTitle(getString(R.string.Deleting));
        progressDialog3.setMessage(getString(R.string.contactsDeleting));
        // progressDialog3.setProgressStyle(progressDialog3.STYLE_HORIZONTAL);
        progressDialog3.setMax(100);
        progressDialog3.show();
        progressDialog3.setCancelable(false);
        progressDialog3.setCanceledOnTouchOutside(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress < 100)
                {
                    try {
                        Thread.sleep(200);
                        progress++;
                        progressDialog3.setProgress(progress);
                        if (progress == 100) {
                            progressDialog3.dismiss();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

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
                        continue;
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toast toast = Toast.makeText(getActivity(), R.string.duplicatedDeleted, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
        toast.show();


    }


    public void bringContacts(){

        final ProgressDialog progressDialog4 = new ProgressDialog(getActivity());

        progressDialog4.setTitle(getString(R.string.uploading));
        progressDialog4.setMessage(getString(R.string.ContactAreUploading));
       // progressDialog4.setProgressStyle(progressDialog4.STYLE_HORIZONTAL);
        progressDialog4.setMax(100);
        progressDialog4.show();
        progressDialog4.setCancelable(false);
        progressDialog4.setCanceledOnTouchOutside(false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress4 < 100)
                {
                    try {
                        Thread.sleep(200);
                        progress4++;
                        progressDialog4.setProgress(progress);
                        if (progress4 == 100) {
                            progressDialog4.dismiss();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();



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

        final ProgressDialog progressDialog4 = new ProgressDialog(getActivity());

        progressDialog4.setTitle(getString(R.string.uploading));
        progressDialog4.setMessage(getString(R.string.ContactAreUploading));
        // progressDialog4.setProgressStyle(progressDialog4.STYLE_HORIZONTAL);
        progressDialog4.setMax(100);
        progressDialog4.show();
        progressDialog4.setCancelable(false);
        progressDialog4.setCanceledOnTouchOutside(false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress4 < 100)
                {
                    try {
                        Thread.sleep(200);
                        progress4++;
                        progressDialog4.setProgress(progress);
                        if (progress4 == 100) {
                            progressDialog4.dismiss();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

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
            phone.close();

            for (int i = 0; i < bringPhone.size() ; i++) {
                if (phoneDup.contains(bringPhone.get(i))){
                    continue;
                }
                for (int j = i+1 ; j < bringPhone.size() ; j++) {
                    if (bringPhone.get(i).equals(bringPhone.get(j)) && !phoneDup.contains(bringPhone.get(i))){
                        phoneDup.add(bringPhone.get(i));
                        nameDup.add(bringName.get(i));
                    }
                }


     //           if (name != null && number != null) {

         //       }

            }

            for (int i = 0; i < nameDup.size(); i++) {
                id = reference.push().getKey();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(validUser).child("contacts").child(id);
                Contact contact = new Contact(id, nameDup.get(i), phoneDup.get(i));

                databaseReference.setValue(contact);
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
      //  progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress5 < 100)
                {
                    try {
                        Thread.sleep(200);
                        progress5++;
                        progressDialog.setProgress(progress);
                        if (progress5 == 100) {
                            progressDialog.dismiss();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();


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
                Toast toast = Toast.makeText(getActivity(), "Contacts are uploaded to your phone book.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,600);
                toast.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void importContacts2(){

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
                    }
                }

                System.out.println(userName);



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
                        } finally {
                            progressDialog.dismiss();
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    class ExampleThread extends Thread{
        @Override
        public void run() {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                String name = null;
                String number = null;

                Cursor phone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

                while (phone.moveToNext()) {

                    name = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
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
            }
        }
    }
    }

