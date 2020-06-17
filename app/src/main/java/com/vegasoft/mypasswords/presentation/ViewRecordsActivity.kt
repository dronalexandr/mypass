package com.vegasoft.mypasswords.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bioniex.sextructor.data.exceptions.simpleExceptionHandler
import com.google.gson.GsonBuilder
import com.vegasoft.mypasswords.R
import com.vegasoft.mypasswords.data.persistence.SecureDatabase
import com.vegasoft.mypasswords.presentation.fragments_new.records.view.ViewRecordFragment.Companion.newInstance
import com.vegasoft.mypasswords.presentation.ui_models.SecuredData
import com.vegasoft.mypasswords.presentation.ui_models.UIRecord
import com.vegasoft.mypasswords.utils.getCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.relex.circleindicator.CircleIndicator
import java.util.*

class ViewRecordsActivity : AppCompatActivity(), OnListFragmentInteractionListener {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null

    private val mScope = getCoroutineScope()
    private fun gerErrorHandler() = simpleExceptionHandler { error ->
        mScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ViewRecordsActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_records)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.container)
        mViewPager?.adapter = mSectionsPagerAdapter
        val indicator = findViewById<CircleIndicator>(R.id.indicator)
        indicator.setViewPager(mViewPager)
        mSectionsPagerAdapter?.registerDataSetObserver(indicator.dataSetObserver)
    }

    override fun onStart() {
        super.onStart()
        reloadData()
    }

    private fun reloadData() {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val records = SecureDatabase.getInstance(this@ViewRecordsActivity).recordsDao().getRecords()
            val uiRecords = arrayListOf<UIRecord>()
            val gson = GsonBuilder().create()
            records.forEach { record ->
                uiRecords.add(UIRecord(
                        record.id,
                        record.title,
                        gson.fromJson(record.data, SecuredData::class.java),
                        record.image,
                        record.userId
                ))
            }
            withContext(Dispatchers.Main) {
                mSectionsPagerAdapter?.setRecords(uiRecords)
                val b = intent.extras
                if (b != null) {
                    mSectionsPagerAdapter?.setSelected(b.getParcelable<Parcelable>(ARG_RECORD) as UIRecord?)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_view_records, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_add) {
            val returnIntent = Intent()
            setResult(MainActivity.RESULT_CODE_ACTIVITY, returnIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: UIRecord) {
        //stub
    }

    override fun saveClick() {
        reloadData()
        Toast.makeText(this, "saved!", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    override fun removeClick(mRecord: UIRecord) {
        val recordsDao = SecureDatabase.getInstance(this@ViewRecordsActivity).recordsDao()
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            recordsDao.deleteRecordById(mRecord.id)
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }

    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        val mRecords = ArrayList<UIRecord>()
        fun setRecords(records: ArrayList<UIRecord>) {
            this.mRecords.clear()
            this.mRecords.addAll(records)
            notifyDataSetChanged()
        }

        fun setSelected(record: UIRecord?) {
            for (i in mRecords.indices) {
                if (mRecords[i].id == record?.id) {
                    mViewPager?.currentItem = i
                    return
                }
            }
        }

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return newInstance(mRecords[position])
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return mRecords.size
        }
    }

    companion object {
        const val ARG_RECORD = "record"
    }
}