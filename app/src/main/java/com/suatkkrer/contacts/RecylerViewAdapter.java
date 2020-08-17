package com.suatkkrer.contacts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Contact> mData;
    Dialog myDialog;
    private ArrayList<String> userName;
    private ArrayList<String> userMail;
//    private ArrayList<String> userID;
    private OnNoteListener onNoteListener;


    public RecylerViewAdapter(ArrayList<String> userName, ArrayList<String> userMail,OnNoteListener onNoteListener) {
        this.userName = userName;
        this.userMail = userMail;
//        this.userID = userid;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


//        View v;
//        v = LayoutInflater.from(mContext).inflate(R.layout.item_contact,parent,false);
//        final MyViewHolder viewHolder = new MyViewHolder(v);

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_contact,parent,false);
//        final MyViewHolder viewHolder = new MyViewHolder(view);


//        myDialog = new Dialog(mContext);
//        myDialog.setContentView(R.layout.dialog_contact);
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//
//        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView dialog_name_tv = myDialog.findViewById(R.id.dialog_name_id);
//                TextView dialog_phone_tv = myDialog.findViewById(R.id.dialog_phone_id);
//                dialog_name_tv.setText(mData.get(viewHolder.getAdapterPosition()).getName());
//                dialog_phone_tv.setText(mData.get(viewHolder.getAdapterPosition()).getPhone());
//                Toast.makeText(mContext, "Test Click" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                myDialog.show();
//            }
//        });

        return new MyViewHolder(view,onNoteListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(userName.get(position));
        holder.tv_phone.setText(userMail.get(position));

    }

    @Override
    public int getItemCount() {
        return userName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private LinearLayout item_contact;
        private TextView tv_name;
        private TextView tv_phone;
        OnNoteListener onNoteListener;

        public MyViewHolder(View itemView,OnNoteListener onNoteListener){
            super(itemView);

            tv_name = itemView.findViewById(R.id.name_contact);
            tv_phone = itemView.findViewById(R.id.phone_contact);
            item_contact = itemView.findViewById(R.id.contact_item_id);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
    public interface  OnNoteListener{
        void onNoteClick(int position);
    }
}
