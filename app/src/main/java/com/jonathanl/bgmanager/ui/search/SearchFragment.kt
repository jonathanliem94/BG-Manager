package com.jonathanl.bgmanager.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.ui.home.SearchViewModel
import com.jonathanl.bgmanager.ui.search.recyclerview.SearchViewAdapter

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        searchViewModel.text.observe(this, Observer {
            textView.text = it
        })

        viewManager = LinearLayoutManager(this.context)
        //Placeholder input of 25 numbers for the recyclerview)
        viewAdapter = SearchViewAdapter(Array(25){
            i -> (i+1).toString()
        })
        recyclerView = root.findViewById<RecyclerView>(R.id.searchPage_recyclerView).apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter
            adapter = viewAdapter
        }


        return root
    }

}