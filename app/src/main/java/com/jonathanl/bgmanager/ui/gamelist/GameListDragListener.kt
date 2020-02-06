package com.jonathanl.bgmanager.ui.gamelist

import androidx.recyclerview.widget.RecyclerView

interface GameListDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

}