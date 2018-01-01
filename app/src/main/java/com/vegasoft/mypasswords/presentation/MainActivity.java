package com.vegasoft.mypasswords.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.bussiness.ConfigManager;
import com.vegasoft.mypasswords.data.entity.Encryption;
import com.vegasoft.mypasswords.data.entity.Record;
import com.vegasoft.mypasswords.presentation.fragments.RecordListFragment;
import com.vegasoft.mypasswords.presentation.fragments.SettingsFragment;
import com.vegasoft.mypasswords.presentation.fragments.ViewRecordFragment;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    public static final int RESULT_VIEW_ACTIVITY = 888;
    public static final int RESULT_CODE_ACTIVITY = 111;

    private android.support.v4.app.Fragment currentFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (currentFragment instanceof RecordListFragment) {
                        return true;
                    }
                    currentFragment = RecordListFragment.newInstance();
                    switchFragment(currentFragment);
                    return true;
                case R.id.navigation_add:
                    if (currentFragment instanceof ViewRecordFragment) {
                        return true;
                    }
                    currentFragment = ViewRecordFragment.newInstance(null);
                    switchFragment(currentFragment);
                    return true;
                case R.id.navigation_settings:
                    if (currentFragment instanceof SettingsFragment) {
                        return true;
                    }
                    currentFragment = SettingsFragment.newInstance();
                    switchFragment(currentFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_VIEW_ACTIVITY) {
            if (RESULT_CODE_ACTIVITY == resultCode) {
                ((BottomNavigationView) findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_add);
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        ConfigManager configManager = new ConfigManager(this);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_rsa:
                if (checked)
                    configManager.saveEncryption(Encryption.RSA);
                    break;
            case R.id.radio_aes:
                if (checked)
                    configManager.saveEncryption(Encryption.AES);
                    break;
        }
    }

    @Override
    public void onItemClick(Record item) {
        Intent intent = new Intent(this, ViewRecordsActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(ViewRecordsActivity.ARG_RECORD, item); //Your id
        intent.putExtras(b);
        startActivityForResult(intent, RESULT_VIEW_ACTIVITY);
    }

    @Override
    public void saveClick() {
        ((BottomNavigationView) findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_home);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.container, fragment).commitAllowingStateLoss();
    }
}
