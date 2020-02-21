package com.jonathanl.bgmanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.databinding.FragmentSearchBinding
import com.jonathanl.bgmanager.di.DaggerSearchComponent
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.network.BoardGameSearchResults
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    @Inject
    lateinit var searchViewModel: SearchViewModel

    private lateinit var recyclerView: RecyclerView
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
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
            adapter = SearchViewAdapter(searchViewModel)
        }

        // Subscribe to search results in sharedviewmodel
        compositeDisposable.add(subscribeToSearchResults())
    }

    private fun subscribeToSearchResults(): Disposable {
        return searchViewModel.boardGameSearchResults
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
    }

    private fun handleSearchResults(results: BoardGameSearchResults) {
        if ((results.resultsArray.isNullOrEmpty()).not()) {
            (recyclerView.adapter as SearchViewAdapter).submitList(results.resultsArray)
            searchViewModel.setVisibilityAfterSearchWithResults()
        }
        else {
            searchViewModel.setVisibilityAfterSearchWithNoResults()
        }
    }

    private fun setUpDI() {
        DaggerSearchComponent.builder()
            .mainActivityComponent(getMainActivityComponent())
            .build()
            .inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}