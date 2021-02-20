package com.bombadu.photograbic.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bombadu.photograbic.R
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso

class FullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
        supportActionBar?.hide()
        val imageView = findViewById<ZoomageView>(R.id.full_zoom_image_view)
        val bundle = intent.extras

        val imageUrl = bundle?.getString("image_url")
        Picasso.get().load(imageUrl).into(imageView)
    }
}