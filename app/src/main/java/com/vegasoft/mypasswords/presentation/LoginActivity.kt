package com.vegasoft.mypasswords.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.vegasoft.mypasswords.R
import com.vegasoft.mypasswords.bussiness.ConfigManager
import kotlinx.android.synthetic.main.activity_login.password as mPasswordView
import kotlinx.android.synthetic.main.activity_login.email_sign_in_button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mPasswordView.setOnEditorActionListener(OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        actionBar?.hide()
        email_sign_in_button.setOnClickListener { attemptLogin() }
        if (isPswdSet()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
    }

    private fun attemptLogin() {
        mPasswordView.error = null
        val password = mPasswordView.text.toString()
        if (!isPasswordValid(password)) {
            mPasswordView.error = getString(R.string.error_invalid_password)
            return
        }

        if (!isPasswordCorrect(password)) {
            mPasswordView.error = getString(R.string.error_incorrect_password)
            return
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isPswdSet() = TextUtils.isEmpty(ConfigManager(this).pswd)

    private fun isPasswordCorrect(password: String) = ConfigManager(this).pswd == password

    private fun isPasswordValid(password: String) = password.length >= 4
}