package com.jonathanl.bgmanager.ui.search

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.data.models.BoardGameSearchResults
import com.jonathanl.bgmanager.databinding.FragmentSearchBinding
import com.jonathanl.bgmanager.di.DaggerSearchComponent
import com.jonathanl.bgmanager.useCases.SEARCH_START
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchFragment : BaseFragment() {

    @Inject
    lateinit var searchViewModel: SearchViewModel

    // Binding exists between onCreateView and onDestroyView
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
        //ViewBinding, inflating the view, and setting the viewmodel
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewSearch.apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(this.context)
            // specify an viewAdapter
            adapter = SearchViewAdapter(searchViewModel)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        }

        setUpInitialUI()
        compositeDisposable.addAll(subscribeToSearchResults(), subscribeToSearchStatus())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_search, menu)
        val searchView = (menu.findItem(R.id.search_option).actionView as SearchView)
        searchView.apply {
            queryHint = getString(R.string.search_hint)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchViewModel.conductBoardGameSearch(query)
                    //ensure focus on search bar is lost after a search
                    this@apply.clearFocus()
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
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

    private fun subscribeToSearchStatus(): Disposable {
        return searchViewModel.boardGameSearchStatus
            .subscribeBy (
                onNext = {
                    when (it) {
                        SEARCH_START -> setVisibilityDuringSearch()
                    }
                },
                onError = {
                    Log.e("SearchViewModel", "subscribeToSearchStatus failed. $it")
                }
            )
    }

    private fun handleSearchResults(results: BoardGameSearchResults) {
        if ((results.resultsArray.isNullOrEmpty()).not()) {
            (binding.recyclerViewSearch.adapter as SearchViewAdapter).submitList(results.resultsArray)
            setVisibilityAfterSearchWithResults()
        }
        else {
            setVisibilityAfterSearchWithNoResults()
        }
    }

    private fun setUpInitialUI() {
        binding.run {
            progressBar.visibility = View.INVISIBLE
            searchStatusText.text = getString(R.string.search_start_up)
            searchStatusText.visibility = View.VISIBLE
            recyclerViewSearch.visibility = View.VISIBLE
        }
    }

    private fun setVisibilityDuringSearch() {
        binding.run {
            progressBar.visibility = View.VISIBLE
            searchStatusText.text = getString(R.string.search_in_progress)
            searchStatusText.visibility = View.VISIBLE
            recyclerViewSearch.visibility = View.GONE
        }
    }

    private fun setVisibilityAfterSearchWithResults() {
        binding.run {
            progressBar.visibility = View.GONE
            searchStatusText.visibility = View.GONE
            recyclerViewSearch.visibility = View.VISIBLE
        }
    }

    private fun setVisibilityAfterSearchWithNoResults() {
        binding.run {
            progressBar.visibility = View.INVISIBLE
            searchStatusText.text = getString(R.string.search_no_results)
            searchStatusText.visibility = View.VISIBLE
            recyclerViewSearch.visibility = View.GONE
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
        _binding = null
        compositeDisposable.clear()
    }

}