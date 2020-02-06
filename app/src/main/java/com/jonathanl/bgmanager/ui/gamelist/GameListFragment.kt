package com.jonathanl.bgmanager.ui.gamelist

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R

class GameListFragment : Fragment(), GameListDragListener {

    private val gameListViewModel: GameListViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_game_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_gamelist)
        gameListViewModel.text.observe(this, Observer {
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

        // test input
        (recyclerView.adapter as GameListViewAdapter).submitList(
            mutableListOf(
                GameListEntry("Gloomhaven", "123"),
                GameListEntry("Catan", "456"),
                GameListEntry("Pandemic", "789")
            )
        )
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}