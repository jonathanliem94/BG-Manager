package com.jonathanl.bgmanager.ui.gamelist

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

class GameListGestureCallback(private val adapter: GameListViewAdapter) : ItemTouchHelper.Callback() {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.insertEntryOnDrag(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    // declare what move directions are allowed for EACH state
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragMovementFlags = UP or DOWN
        val swipeMovementFlags = RIGHT
        return makeMovementFlags(dragMovementFlags, swipeMovementFlags)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeEntryOnSwipe(viewHolder.adapterPosition)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

}