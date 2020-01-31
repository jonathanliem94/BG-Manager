package com.jonathanl.bgmanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.SharedViewModel
import com.jonathanl.bgmanager.ui.home.SearchViewModel
import com.jonathanl.bgmanager.ui.search.recyclerview.SearchViewAdapter
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        val textView: TextView = root.findViewById(R.id.text_home)
        searchViewModel.text.observe(this, Observer {
            textView.text = it
        })

        recyclerView = root.findViewById<RecyclerView>(R.id.searchPage_recyclerView).apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(this.context)
            // specify an viewAdapter
            adapter = SearchViewAdapter()
        }

        // Subscribe to search results in sharedviewmodel
        subscribeToSearchResults()

        return root
    }

    private fun subscribeToSearchResults() {
        disposable = sharedViewModel.searchResults
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onNext = {
                    this.activity?.runOnUiThread {
                        (recyclerView.adapter as SearchViewAdapter).submitList(it.resultsArray)
                    }
                },
                onError = {
                    Log.e("SearchFragment", "subscribe to search results failed, $it")
                }
            )
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }

}