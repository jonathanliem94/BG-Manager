package com.jonathanl.bgmanager.ui.gamelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.databinding.FragmentGameListBinding
import com.jonathanl.bgmanager.di.DaggerGameListComponent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameListFragment : BaseFragment(), GameListDragListener {

    @Inject
    lateinit var gameListViewModel: GameListViewModel
    private var _binding: FragmentGameListBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView stuff
        val gameListAdapter = GameListViewAdapter(this)
        val gestureCallback = GameListGestureCallback(gameListAdapter)
        itemTouchHelper = ItemTouchHelper(gestureCallback)
        binding.recyclerViewGameList.apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(this.context)
            // specify an viewAdapter
            adapter = gameListAdapter
        }
        // Attach gesture functionality (swipe and drag and drop) to recycler view
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewGameList)
        compositeDisposable.addAll(subscribeToGameListHolder())
    }

    private fun subscribeToGameListHolder(): Disposable{
        return gameListViewModel.gameListHolder
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onNext = {
                    (binding.recyclerViewGameList.adapter as GameListViewAdapter).submitList(it)
                },
                onError = {
                    Log.e("GameListFragment", "subscribeToGameListObservable failed. $it")
                }
            )
    }

    private fun setUpDI() {
        DaggerGameListComponent.builder()
            .mainActivityComponent(getMainActivityComponent())
            .build()
            .inject(this)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        _binding = null
        super.onDestroyView()
    }
}

interface GameListDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

}