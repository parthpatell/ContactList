package com.example.contactlist;

import java.util.ArrayList;

public class Contact {
    String name;
    String phoneNum;
    Boolean checked = Boolean.FALSE;
    String relationships;
    Integer ID;

    public Contact(String name, String phone, String relationships, Integer ID){
        this.name = name;
        this.phoneNum = phone;
        this.relationships = relationships;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public Boolean isChecked(){
        return checked;
    }
}
