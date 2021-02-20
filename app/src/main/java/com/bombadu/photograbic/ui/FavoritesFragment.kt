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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class FavoritesFragment : Fragment(), FavoritesAdapter.ItemClickListener {
    private lateinit var favAdapter: FavoritesAdapter
    lateinit var viewModel: ImageViewModel

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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