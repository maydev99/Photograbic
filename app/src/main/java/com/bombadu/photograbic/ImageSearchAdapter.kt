package com.bombadu.photograbic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageSearchAdapter : RecyclerView.Adapter<ImageSearchAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object  : ItemCallback<ImageResponse.Hit>() {
        override fun areItemsTheSame(oldItem: ImageResponse.Hit, newItem: ImageResponse.Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageResponse.Hit, newItem: ImageResponse.Hit): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    var images: List<ImageResponse.Hit>
        get() = differ.currentList
        set(value) { differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_item,
                parent,
                false
            )

        )
    }

    private var onItemClickListener: ((ImageResponse.Hit) -> Unit)? = null

    fun setOnItemClickListener(listener: (ImageResponse.Hit) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.itemView.apply {
            println(images)
            val itemImageView = findViewById<ImageView>(R.id.item_image_view)
            val item = images[position]
            println("HEIGHT: ${item.previewURL}")
            Picasso.get().load(item.previewURL).into(itemImageView)

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(item)
                }
            }

        }


    }

    override fun getItemCount(): Int {
        return images.size
    }


}