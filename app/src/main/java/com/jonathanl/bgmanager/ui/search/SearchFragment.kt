package com.jonathanl.bgmanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.SharedViewModel
import com.jonathanl.bgmanager.databinding.FragmentSearchBinding
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import com.jonathanl.bgmanager.ui.search.recyclerview.SearchViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Databinding, inflating the view, and setting the viewmodel
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewmodel = searchViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.searchPage_recyclerView).apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(this.context)
            // specify an viewAdapter
            adapter = SearchViewAdapter()
        }

        // Subscribe to search results in sharedviewmodel
        subscribeToSearchResults()
        subscribeToWhenSearchIsInvoked()
    }

    private fun subscribeToSearchResults() {
        val disposable: Disposable = sharedViewModel.searchResults
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    handleSearchResults(it)
                },
                onError = {
                    Log.e("SearchFragment", "subscribe to search results failed, $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun subscribeToWhenSearchIsInvoked() {
        val disposable: Disposable = sharedViewModel.searchQueryPublishSubject
            .subscribeBy (
                onNext = {
                    this.activity?.runOnUiThread {
                        searchViewModel.setVisibilityDuringSearch()
                    }
                },
                onError = {
                    Log.e("SearchFragment", "subscribe to when search is invoked failed, $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun handleSearchResults(results: BoardGameSearchResults) {
        if (results.resultsArray.isNullOrEmpty()) {
            searchViewModel.setVisibilityAfterSearchWithNoResults()
        } else {
            (recyclerView.adapter as SearchViewAdapter).submitList(results.resultsArray)
            searchViewModel.setVisibilityAfterSearchWithResults()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}