package com.vegasoft.mypasswords.bussiness;

import android.content.Context;
import android.content.SharedPreferences;

import com.vegasoft.mypasswords.data.entity.Encryption;

public class ConfigManager {
    private SharedPreferences sharedPref;
    private static final String KEY_USER = "user";
    private static final String KEY_PSW = "passwd";
    private static final String KEY_ENCRYPTION = "encryption";

    public ConfigManager(Context context) {
        sharedPref = context.getSharedPreferences(
                "com.vegasoft.mypasswords.PasswordManager", Context.MODE_PRIVATE);
    }

    public void saveDefaultUser(String user) {
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_USER, user);
        editor.apply();
    }

    public String getDefaultUser() {
        return sharedPref.getString(KEY_USER, "");
    }

    public void savePSWD(String pswd) {
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_PSW, pswd);
        editor.apply();
    }

    public String getPSWD() {
        return sharedPref.getString(KEY_PSW, "");
    }

    public void saveEncryption(Encryption encryption) {
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_ENCRYPTION, encryption.name());
        editor.apply();
    }

    public Encryption getEncryption() {
        final String value = sharedPref.getString(KEY_ENCRYPTION, "");
        switch (value) {
            case "AES":
                return Encryption.AES;
            case "RSA":
                return Encryption.RSA;
        }
        return Encryption.RSA;
    }
}
