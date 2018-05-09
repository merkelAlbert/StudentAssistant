package com.assistant.albert.studentassistant.authentification;

import android.widget.Toast;

import com.assistant.albert.studentassistant.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserItem {
    private String email;
    private String password;

    public UserItem(String email, String password) {
        this.email = email;
        this.password = Utils.md5(password);
    }



    public String Email() {
        return email;
    }

    public String Password() {
        return password;
    }
}
