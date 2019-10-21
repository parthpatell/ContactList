package com.example.contactlist;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class contactProfileFragment extends Fragment {

    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String RELATIONSHIP = "relationship";

    private String mName;
    private String mNumber;
    private ArrayList<String> mList;

    public ListView contact_relationships;
    public TextView name_fragment;
    public TextView number_fragment;

    private ArrayList<Contact> contacts = new ArrayList<>();



    public contactProfileFragment() {
        // Required empty public constructor
    }

    public static contactProfileFragment newInstance(String names, String number, ArrayList<String> List) {
        contactProfileFragment fragment = new contactProfileFragment();
        Bundle args = new Bundle();

        args.putString("name", names);
        args.putString(NUMBER, number);
        args.putStringArrayList(RELATIONSHIP, List);

        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){

            mName = getArguments().getString(NAME);
            mNumber = getArguments().getString(NUMBER);
            mList = getArguments().getStringArrayList(RELATIONSHIP);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contactprofile, container, false);

        contact_relationships = view.findViewById(R.id.relationshipList);
        name_fragment = view.findViewById(R.id.NameFragment);
        number_fragment = view.findViewById(R.id.PhoneFragment);

        name_fragment.setText(mName);
        number_fragment.setText(mNumber);

        final CustomAdapter customAdapter = new CustomAdapter();
        contact_relationships.setAdapter(customAdapter);

        return view;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }



    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((long) position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.frag_relationship, null);

            TextView contact_name = convertView.findViewById(R.id.fraglistContact_name);
            System.out.println(mList);
            contact_name.setText(mList.get(position));


            contact_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    //openFragment(mList.get(position));
                }
            });

            return convertView;
        }
    }

}
