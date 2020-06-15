package com.vegasoft.mypasswords.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vegasoft.mypasswords.R
import com.vegasoft.mypasswords.bussiness.ConfigManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(R.layout.activity_splash)
        checkPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleSecureInput()
                } else {
                    finish()
                }
            }
        }
    }

    private fun handleSecureInput() {
        if (isFirstStart()) startMainActivityForDelay() else startLoginActivityForDelay()
    }

    private fun startLoginActivityForDelay() {
        Handler().postDelayed({
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }, 2000)
    }

    private fun startMainActivityForDelay() {
        Handler().postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 2000)
    }

    private fun isFirstStart() = TextUtils.isEmpty(ConfigManager(this).pswd)

    private fun checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_WRITE_STORAGE)
            }
        } else {
            handleSecureInput()
        }
    }

    private fun showExplanation() {
        AlertDialog.Builder(this)
                .setTitle("Request Permission")
                .setMessage("Please, enable access to the storage!\nWe need permissions for using images!")
                .setPositiveButton(android.R.string.yes) { _, _ -> checkPermission() }
                .setNegativeButton(android.R.string.no) { _, _ -> finish() }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    companion object {
        private const val REQUEST_WRITE_STORAGE = 1
    }
}