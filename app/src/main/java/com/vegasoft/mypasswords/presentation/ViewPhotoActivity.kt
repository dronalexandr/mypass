package com.vegasoft.mypasswords.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.vegasoft.mypasswords.R
import com.vegasoft.mypasswords.presentation.custom_view.CustomImageView
import java.io.File

class ViewPhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photo)
        loadImage(intent.extras?.getString(ARG_PHOTO))
    }

    private fun loadImage(url: String?) {
        if (url == null) return
        val imageView = findViewById<CustomImageView>(R.id.photo_button)
        Picasso.get().load(File(url)).into(imageView)
        findViewById<View>(R.id.photo_button).setOnClickListener { finish() }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        hide()
    }

    private fun hide() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    companion object {
        @JvmField
        var ARG_PHOTO = "photo_arguments"
    }
}