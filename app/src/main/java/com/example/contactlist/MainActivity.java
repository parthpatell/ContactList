package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_LIST = "list";
    private static final String KEY_CHECKED = "checked";


    private SQLiteDatabase mDatabase;
    ContactDBHelper dbHelper;
    ListView listView;
    ArrayList<Contact> contacts = new ArrayList<>();
    ArrayList<Boolean> checked = new ArrayList<>();
    CustomAdapter customAdapter;

    private FrameLayout fragmentContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.contact_list);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        dbHelper = new ContactDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        viewData();

        Button addButton = findViewById(R.id.add_button);
        Button deleteButton = findViewById(R.id.delete_button);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int counter = 0;
                System.out.println("THE SIZE IS:" +contacts.size());
                for (int i=contacts.size()-1;i>=0; i--){
                    System.out.println(contacts.get(i).name +" is " + contacts.get(i).checked);
                    if (contacts.get(i).checked) {
                        contacts.remove(i);
                        customAdapter.notifyDataSetChanged();

                        dbHelper.deleteTitle(contacts.get(i).ID);
                        counter++;
                    }
                }
                Collections.sort(contacts, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact o1, Contact o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                });

                customAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Deleted "+counter+" item(s)", Toast.LENGTH_LONG).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openContactDetails();
            }


        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        viewData();
    }

    private void viewData() {
        Cursor cursor = dbHelper.viewData();

        if (cursor.getCount() == 0 ){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
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
            listView.setAdapter(customAdapter);
        }
    }

    private void openContactDetails() {
        Intent intent = new Intent(this, activity_Contact_Details.class);
        startActivity(intent);
    }

    public void openFragment(Contact person){
        String packed = person.relationships;
        System.out.println("THE PACKED STRING IS: "+packed);
        ArrayList<String> unpacked = unpack(packed);

        contactProfileFragment fragment = contactProfileFragment.newInstance(person.name,person.phoneNum, unpacked);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.fragmentContainer, fragment, "CONTACT_PROFILE_FRAGMENT").commit();
    }

    public ArrayList<String> unpack(String packed){
        ArrayList<String> output = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(packed, " ,");
        String tokens;
        while (st.hasMoreTokens()) {
            tokens = st.nextToken();
            if (tokens.length() != 0 ){
                dbHelper.getName(tokens);
            }
        }
        return output;
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
                        System.out.println("Checked! "+contacts.get(position).name);
                        notifyDataSetChanged();
                    } else {
                        contacts.get(position).checked = Boolean.FALSE;
                        notifyDataSetChanged();
                    }
                }
            });

            contact_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    openFragment(contacts.get(position));
                }
            });

            return convertView;
        }
    }
//For storing the orientations.
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putStringArray(KEY_LIST, listView);
//        outState.putString(KEY_CHECKED, checked);}
//

}

