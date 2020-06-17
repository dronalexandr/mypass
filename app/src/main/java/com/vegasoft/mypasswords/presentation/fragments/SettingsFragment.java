package com.vegasoft.mypasswords.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.bussiness.ConfigManager;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends Fragment {

    private ConfigManager configManager;
    public SettingsFragment() {
    }
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        configManager = new ConfigManager(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText userNameEditText = view.findViewById(R.id.defaultUser_editText);
        EditText passwordEditText = view.findViewById(R.id.password_editText);
        userNameEditText.setText(configManager.getDefaultUser());
        passwordEditText.setText(configManager.getPSWD());
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                configManager.saveDefaultUser(editable.toString());
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                configManager.savePSWD(editable.toString());
            }
        });

        switch (configManager.getEncryption()){
            case AES:
                ((RadioButton) view.findViewById(R.id.radio_aes)).setChecked(true);
                break;
            case RSA:
                ((RadioButton) view.findViewById(R.id.radio_rsa)).setChecked(true);
                break;
        }
    }
}
