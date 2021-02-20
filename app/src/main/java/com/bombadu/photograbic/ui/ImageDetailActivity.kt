package com.bombadu.photograbic.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bombadu.photograbic.R
import com.bombadu.photograbic.databinding.ActivityImageDetailBinding
import com.bombadu.photograbic.local.LocalData
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDetailBinding
    private var isFavorite = false
    private lateinit var viewModel: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        myBundle()

        if (isFavorite) {
            binding.favoriteImageView.setImageResource(R.drawable.ic_outline_favorite_24)
        }

        binding.favoriteImageView.setOnClickListener {
            if (!isFavorite) {
                isFavorite = true
                binding.favoriteImageView.setImageResource(R.drawable.ic_outline_favorite_24)
                saveToLocalDB()
            }

        }

        binding.webImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(largeImageUrl)
            startActivity(intent)
        }

        binding.shareImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Link to Image\n\n$largeImageUrl")
            intent.type = "text/plain"
            startActivity(intent)
        }

        binding.fullscreenImageView.setOnClickListener {
            val intent = Intent(this, FullScreenActivity::class.java)
            val bundle = Bundle()
            bundle.putString("image_url", largeImageUrl)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }


    private fun saveToLocalDB() {
        val newEntry = LocalData(userImage, largeImageUrl, pageUrl, userName, false)
        viewModel.insertImageDataIntoDB(newEntry)
    }



    private fun myBundle() {
        val bundle = intent.extras
        largeImageUrl = bundle?.getString("largeImage").toString()
        userImage = bundle?.getString("userImage").toString()
        pageUrl = bundle?.getString("pageImage").toString()
        userName = bundle?.getString("username").toString()
        isFavorite = bundle?.getBoolean("favorite", false)!!
        binding.userTextView.text = userName
        Picasso.get().load(largeImageUrl).into(binding.zoomImageView)
        if (userImage.isNotEmpty()) {
            Picasso.get().load(userImage).into(binding.userImageView)
        }

        println(isFavorite)

        if (isFavorite) {
            binding.favoriteImageView.setImageResource(R.drawable.ic_outline_favorite_24)
            //binding.favoriteImageView.visibility = View.GONE
        }

    }

    companion object {
        private lateinit var largeImageUrl: String
        private lateinit var userImage: String
        private lateinit var pageUrl: String
        private lateinit var userName: String
    }
}