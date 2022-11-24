package com.example.merchant.models;

public class MerchantModel {
    int idMerchant;
    String name, email, contact,password;

    public MerchantModel(int idMerchant, String name, String email, String contact, String password) {
        this.idMerchant = idMerchant;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
    }

    public MerchantModel(String name, String email, String contact, String password) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
    }

    public MerchantModel(){}

    public int getIdMerchant() {
        return idMerchant;
    }

    public void setIdMerchant(int idMerchant) {
        this.idMerchant = idMerchant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
