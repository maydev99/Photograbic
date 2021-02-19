package com.bombadu.photograbic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bombadu.photograbic.databinding.ActivityImageDetailBinding
import com.squareup.picasso.Picasso


class ImageDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDetailBinding
    private lateinit var largeImageUrl: String
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myBundle()

        binding.favoriteImageView.setOnClickListener {
            if (!isFavorite) {
                isFavorite = true
                binding.favoriteImageView.setImageResource(R.drawable.ic_outline_favorite_24)
                saveToLocalDB()
            } else {
                isFavorite = false
                binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                deleteFromLocalDB()
            }
        }
    }

    private fun deleteFromLocalDB() {

    }

    private fun saveToLocalDB() {

    }

    private fun myBundle() {
        val bundle = intent.extras
        largeImageUrl = bundle?.getString("largeImage").toString()
        val userImage = bundle?.getString("userImage").toString()
        val pageUrl = bundle?.getString("pageImage").toString()

        binding.userTextView.text = bundle?.getString("username").toString()
        Picasso.get().load(largeImageUrl).into(binding.zoomImageView)
        if (userImage.isNotEmpty()) {
            Picasso.get().load(userImage).into(binding.userImageView)
        } else {
            Picasso.get().load(getString(R.string.missing_user_image)).into(binding.userImageView)
        }

    }
}