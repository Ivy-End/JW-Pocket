package com.jw.iii.pocketjw.Helper.Contact;

/**
 * Created by End on 2015/9/13.
 */
public class ContactItem {

    public ContactItem(String imgUrl, String name, String phone, String email) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    private String imgUrl;
    private String name;
    private String phone;
    private String email;
}
