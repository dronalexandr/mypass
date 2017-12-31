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

import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.data.entity.Record;
import com.vegasoft.mypasswords.presentation.fragments.RecordListFragment;
import com.vegasoft.mypasswords.presentation.fragments.SettingsFragment;
import com.vegasoft.mypasswords.presentation.fragments.ViewRecordFragment;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

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
    public void onItemClick(Record item) {
        startActivity(new Intent(this, ViewRecordsActivity.class));
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
