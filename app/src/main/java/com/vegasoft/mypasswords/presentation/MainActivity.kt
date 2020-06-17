package com.vegasoft.mypasswords.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vegasoft.mypasswords.R
import com.vegasoft.mypasswords.bussiness.ConfigManager
import com.vegasoft.mypasswords.data.persistence.models.Encryption
import com.vegasoft.mypasswords.presentation.fragments_new.records.list.RecordListFragment
import com.vegasoft.mypasswords.presentation.fragments.SettingsFragment
import com.vegasoft.mypasswords.presentation.fragments_new.records.view.ViewRecordFragment
import com.vegasoft.mypasswords.presentation.ui_models.UIRecord
import kotlinx.android.synthetic.main.activity_main.navigation

class MainActivity : AppCompatActivity(), OnListFragmentInteractionListener {
    private var currentFragment: Fragment? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (currentFragment is RecordListFragment) {
                    return@OnNavigationItemSelectedListener true
                }
                currentFragment = RecordListFragment.newInstance()
                switchFragment(currentFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add -> {
                if (currentFragment is ViewRecordFragment) {
                    return@OnNavigationItemSelectedListener true
                }
                currentFragment = ViewRecordFragment.newInstance(null)
                switchFragment(currentFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                if (currentFragment is SettingsFragment) {
                    return@OnNavigationItemSelectedListener true
                }
                currentFragment = SettingsFragment.newInstance()
                switchFragment(currentFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_VIEW_ACTIVITY) {
            if (RESULT_CODE_ACTIVITY == resultCode) {
                navigation.selectedItemId = R.id.navigation_add
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        // Is the button now checked?
        val checked = (view as RadioButton).isChecked
        val configManager = ConfigManager(this)
        when (view.getId()) {
            R.id.radio_rsa -> if (checked) configManager.saveEncryption(Encryption.RSA)
            R.id.radio_aes -> if (checked) configManager.saveEncryption(Encryption.AES)
        }
    }

    override fun onItemClick(item: UIRecord) {
        val intent = Intent(this, ViewRecordsActivity::class.java)
        val b = Bundle()
        b.putParcelable(ViewRecordsActivity.ARG_RECORD, item) //Your id
        intent.putExtras(b)
        startActivityForResult(intent, RESULT_VIEW_ACTIVITY)
    }

    override fun removeClick(mRecord: UIRecord?) {

    }

    override fun saveClick() {
        navigation.selectedItemId = R.id.navigation_home
    }

    private fun switchFragment(fragment: Fragment?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(R.id.container, fragment!!).commitAllowingStateLoss()
    }

    companion object {
        const val RESULT_VIEW_ACTIVITY = 888
        const val RESULT_CODE_ACTIVITY = 111
    }
}