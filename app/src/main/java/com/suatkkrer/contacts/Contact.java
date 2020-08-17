package com.suatkkrer.contacts;

public class Contact {

    private String Name;
    private String Phone;
    private String Id;

    public Contact() {
    }

    public Contact( String id,String name, String phone) {
        Name = name;
        Phone = phone;
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
