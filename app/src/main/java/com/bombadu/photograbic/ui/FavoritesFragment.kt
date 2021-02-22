package com.bombadu.photograbic.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.photograbic.R
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class FavoritesFragment : Fragment(), FavoritesAdapter.ItemClickListener {
    private lateinit var favAdapter: FavoritesAdapter
    lateinit var viewModel: ImageViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)
        favAdapter = FavoritesAdapter(this)
        recyclerView = view.findViewById<RecyclerView>(R.id.favorites_recycler_view)
        setupRecyclerView()
        observeData()



    }

    private fun setupRecyclerView() = recyclerView.apply  {
        favAdapter = FavoritesAdapter(this@FavoritesFragment)
        adapter = favAdapter
        hasFixedSize()
        layoutManager = LinearLayoutManager(requireContext())

        ItemTouchHelper(object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                favAdapter.getItemAt(viewHolder.adapterPosition)
                    .let { viewModel.deleteImageItem(it) }
            }

        }).attachToRecyclerView(recyclerView)

    }

    private fun observeData() {
        viewModel.getAllLocalImages().observe(viewLifecycleOwner,
            { list ->
                list.let {
                    favAdapter.submitList(it)
                }

            })
    }

    companion object {

        lateinit var recyclerView: RecyclerView

    }

    override fun onItemClick(position: Int) {
        val item = favAdapter.getItemAt(position)
        val bundle = Bundle()
        val intent = Intent(requireContext(), ImageDetailActivity::class.java)
        bundle.putString("largeImage", item.largeImageUrl)
        bundle.putString("pageImage", item.webImageUrl)
        bundle.putString("userImage", item.userUrl)
        bundle.putString("username", item.username)
        bundle.putBoolean("favorite", true)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}