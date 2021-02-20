package com.bombadu.photograbic.ui

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.photograbic.R
import com.bombadu.photograbic.databinding.FragmentSearchBinding
import com.bombadu.photograbic.network.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
const val TAG = "MainActivity"

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var imageAdapter: ImageSearchAdapter
    private lateinit var recyclerView: RecyclerView

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
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.search_recycler_view)
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
        println("Query: $query")
        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.searchForImage(query)
            } catch (e: IOException) {
                Log.e(TAG, "IOExceptions, you might not have an internet connection")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                //Log.i("RESPONSE", response.body().toString())
                imageAdapter.images = response.body()!!.hits
                saveSearchQuery(query)
            } else {
                Log.e(TAG, "Response not successful")
            }
            binding.progressBar.isVisible = false
        }
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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