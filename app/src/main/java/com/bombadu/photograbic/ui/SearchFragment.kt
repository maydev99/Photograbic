package com.bombadu.photograbic.ui

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.photograbic.R
import com.bombadu.photograbic.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


const val TAG = "MainActivity"

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var imageAdapter: ImageSearchAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ImageViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.search_recycler_view)
        viewModel = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)
        //val imageSearchET = view.findViewById<TextInputEditText>(R.id.image_search_edit_text)

        setHasOptionsMenu(true)
        setupRecyclerView()
        loadSearchQuery()


        imageAdapter.setOnItemClickListener {
            val bundle = Bundle()
            val intent = Intent(requireContext(), ImageDetailActivity::class.java)
            bundle.putString("largeImage", it.largeImageURL)
            bundle.putString("userImage", it.userImageURL)
            bundle.putString("username", it.user)
            bundle.putString("pageImage", it.pageURL)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun makeSearchQuery(query: String) {

        viewModel.searchForImage(query)
        viewModel.imageData.observe(viewLifecycleOwner, {
            imageAdapter.images = it.hits
        })

        saveSearchQuery(query)

    }

    private fun setupRecyclerView() = recyclerView.apply {
        imageAdapter = ImageSearchAdapter()
        adapter = imageAdapter
        hasFixedSize()
        layoutManager = GridLayoutManager(this.context, 2)

    }

    private fun loadSearchQuery() {
        val prefs = activity?.getSharedPreferences("query", MODE_PRIVATE)
        val query = prefs?.getString("query", "cat")
        query?.let { makeSearchQuery(it) }
    }


    private fun saveSearchQuery(query: String) {
        val prefs = activity?.getSharedPreferences("query", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs!!.edit()
        editor.putString("query", query)
        editor.apply()

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    makeSearchQuery(query)
                    searchView.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        })
    }


}