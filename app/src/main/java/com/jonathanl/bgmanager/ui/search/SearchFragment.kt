package com.jonathanl.bgmanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.MainComponentInjector
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.SharedViewModel
import com.jonathanl.bgmanager.databinding.FragmentSearchBinding
import com.jonathanl.bgmanager.di.DaggerSearchComponent
import com.jonathanl.bgmanager.di.SearchComponent
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var searchViewModel: SearchViewModel
    @Inject
    lateinit var sharedViewModel: SharedViewModel

    private lateinit var recyclerView: RecyclerView
    private val component: SearchComponent =
        DaggerSearchComponent.builder()
            .mainActivityComponent(MainComponentInjector.mainActivityComponent)
            .build()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        component.inject(this)
        //Databinding, inflating the view, and setting the viewmodel
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewmodel = searchViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_search).apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(this.context)
            // specify an viewAdapter
            adapter = SearchViewAdapter(sharedViewModel)
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
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    searchViewModel.setVisibilityDuringSearch()
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