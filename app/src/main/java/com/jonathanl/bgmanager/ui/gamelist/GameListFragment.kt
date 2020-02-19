package com.jonathanl.bgmanager.ui.gamelist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.SharedViewModel
import com.jonathanl.bgmanager.di.DaggerGameListComponent
import com.jonathanl.bgmanager.base.BaseFragment
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameListFragment : BaseFragment(), GameListDragListener {

    @Inject
    lateinit var gameListViewModel: GameListViewModel
    @Inject
    lateinit var sharedViewModel: SharedViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
        val root = inflater.inflate(R.layout.fragment_game_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_gamelist)
        gameListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView stuff
        val gameListAdapter = GameListViewAdapter(this)
        val gestureCallback = GameListGestureCallback(gameListAdapter)
        itemTouchHelper = ItemTouchHelper(gestureCallback)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_game_list).apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(this.context)
            // specify an viewAdapter
            adapter = gameListAdapter
        }
        // Attach gesture functionality (swipe and drag and drop) to recycler view
        itemTouchHelper.attachToRecyclerView(recyclerView)
        subscribeToGameListObservable()
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

    private fun subscribeToGameListObservable(){
        disposable = sharedViewModel.gameListHolder
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onNext = {(recyclerView.adapter as GameListViewAdapter).submitList(it)},
                onError = {
                    Log.e("GameListFragment", "subscribeToGameListObservable failed. $it")
                }
            )
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}


interface GameListDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

}