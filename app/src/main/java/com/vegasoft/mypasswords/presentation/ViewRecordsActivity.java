package com.vegasoft.mypasswords.presentation;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.data.db.RecordsDBHelper;
import com.vegasoft.mypasswords.data.entity.Record;
import com.vegasoft.mypasswords.data.loader.LoaderResult;
import com.vegasoft.mypasswords.data.loader.RecordsLoader;
import com.vegasoft.mypasswords.presentation.fragments.ViewRecordFragment;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ViewRecordsActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    private static final int RECORD_LOADER_ID = 0;
    static final String ARG_RECORD = "record";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private LoaderManager.LoaderCallbacks<LoaderResult<ArrayList<Record>>> loaderCallbacks = new LoaderManager.LoaderCallbacks<LoaderResult<ArrayList<Record>>>() {

        @Override
        public Loader<LoaderResult<ArrayList<Record>>> onCreateLoader(int arg0,
                                                                      Bundle arg1) {
            return new RecordsLoader(ViewRecordsActivity.this);
        }

        @Override
        public void onLoadFinished(
                Loader<LoaderResult<ArrayList<Record>>> loader,
                LoaderResult<ArrayList<Record>> result) {
            if (result.error == null) {
                if (result.data != null) {
                    mSectionsPagerAdapter.setRecords(result.data);
                    Bundle b = getIntent().getExtras();
                    if (b != null) {
                        mSectionsPagerAdapter.setSelected((Record) b.getSerializable(ARG_RECORD));
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<LoaderResult<ArrayList<Record>>> loader) {
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        mSectionsPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(RECORD_LOADER_ID, null,
                loaderCallbacks).forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent returnIntent = new Intent();
            setResult(MainActivity.RESULT_CODE_ACTIVITY, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Record item) {
        //stub
    }

    @Override
    public void saveClick() {
        getSupportLoaderManager().restartLoader(RECORD_LOADER_ID, null,
                loaderCallbacks).forceLoad();
        Toast.makeText(this, "saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeClick(Record mRecord) {
        try {
            new RecordsDBHelper(this).deleteRecord(mRecord.getId());
        } catch (Exception e){
            FirebaseCrashlytics.getInstance().recordException(e);
        } finally {
            onBackPressed();
        }


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Record> records = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void setRecords(ArrayList<Record> records) {
            this.records = new ArrayList<>(records);
            notifyDataSetChanged();
        }

        void setSelected(Record record) {
            for (int i = 0; i < records.size(); i++) {
                if(records.get(i).getId().equals(record.getId())) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ViewRecordFragment.newInstance(records.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return records.size();
        }
    }
}
