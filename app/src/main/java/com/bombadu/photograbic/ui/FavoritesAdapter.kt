package com.bombadu.photograbic.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.photograbic.R
import com.bombadu.photograbic.local.LocalData
import com.squareup.picasso.Picasso

class FavoritesAdapter(
    val mItemClickListener: ItemClickListener,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        //Constants, if any go here

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<LocalData>() {

        override fun areItemsTheSame(oldItem: LocalData, newItem: LocalData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocalData, newItem: LocalData): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return FavoritesHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fav_image_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (holder) {
            is FavoritesHolder -> {
                holder.bind(differ.currentList[position])

            }
        }


    }

    fun getItemAt(position: Int): LocalData {
        return differ.currentList[position]
        //Use this for getting selected item data from Activity/ Fragment
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<LocalData>) {
        differ.submitList(list)
    }

    inner class FavoritesHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)

            }
        }

        fun bind(item: LocalData) = with(itemView) {
            itemView.setOnClickListener {

                interaction?.onItemSelected(adapterPosition, item)
                mItemClickListener.onItemClick(adapterPosition)


            }
            Log.d("DATA", "DATA: $item")
            val imageView = findViewById<ImageView>(R.id.fav_item_image_view)
            Picasso.get().load(item.largeImageUrl).into(imageView)
            //Bind data to views here
            //Example: item_movie_title_text_view.text = item.title


        }


    }

    interface Interaction {
        fun onItemSelected(position: Int, item: LocalData)
    }


}



