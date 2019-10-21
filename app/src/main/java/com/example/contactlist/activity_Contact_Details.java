package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class activity_Contact_Details extends AppCompatActivity {

    ListView check_list;
    EditText nameInput;
    EditText phoneInput;
    ContactDBHelper db;
    ArrayList<Contact> contacts = new ArrayList<>();
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__contact__details);


        db = new ContactDBHelper(this);




        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        Button buttonAddCon = findViewById(R.id.add_con_button);
        check_list = findViewById(R.id.relationshipList);

        viewData();

        buttonAddCon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (nameInput.getText().toString().trim().length() == 0 || phoneInput.getText().toString().trim().length() == 0){
                    Toast.makeText(activity_Contact_Details.this, "Name and Phone number are required!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (db.insertData(nameInput.getText().toString().trim(), phoneInput.getText().toString().trim(), getRelationship())) {
                    Toast.makeText(activity_Contact_Details.this, "Contact Added", Toast.LENGTH_SHORT);
                    nameInput.setText("");
                    phoneInput.setText("");
                    viewData();
                }
            }
        });
    }
    //Getting string containing all the IDs
    private String getRelationship() {
        String output = "";
        long id;
        int count =0;
        for (int i =0; i<contacts.size(); i++){
            if (contacts.get(i).checked){
                System.out.println("Getting ID for" + contacts.get(i).name);
                id = contacts.get(i).ID;
                if (count ==0){
                    output = output+id;
                    count++;
                } else{
                    output = output+", "+id;
                    count++;
                }
            }
        }
        System.out.println("Relationships checked are: "+output);
        return output;
    }

    private void viewData() {
        Cursor cursor = db.viewData();

        if (cursor.getCount() == 0 ){
            Toast.makeText(this, "The is No data", Toast.LENGTH_SHORT).show();
        } else {
            contacts.clear();
            while (cursor.moveToNext()){
                contacts.add( new Contact(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(0)));

            }

            //SORT
            Collections.sort(contacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                }
            });

            customAdapter = new CustomAdapter();
            check_list.setAdapter(customAdapter);
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((long) position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.activity_listview, null);

            TextView contact_name = (TextView) convertView.findViewById(R.id.text_contactName);
            final CheckBox contact_check = (CheckBox) convertView.findViewById(R.id.checkbox_contactName);

            contact_name.setText(contacts.get(position).getName());
            contact_check.setChecked(contacts.get(position).isChecked());

            contact_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((CheckBox) view).isChecked()){
                        contacts.get(position).checked = Boolean.TRUE;
                        notifyDataSetChanged();
                    } else {
                        contacts.get(position).checked = Boolean.FALSE;
                        notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }
    }
}
