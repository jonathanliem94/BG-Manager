package com.jonathanl.bgmanager.ui.gamelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import kotlinx.android.synthetic.main.recycler_gamelist_view.view.*
import kotlin.math.abs

class GameListViewAdapter:
    ListAdapter<GameListEntry, GameListViewAdapter.GameListViewHolder>(GameListResultDiffCallback()) {

    private var entryList = mutableListOf<GameListEntry>()

    // Provide itemcallback to allow DiffUtil to determine what is different from the old and new items
    class GameListResultDiffCallback : DiffUtil.ItemCallback<GameListEntry>() {
        override fun areContentsTheSame(oldItem: GameListEntry, newItem: GameListEntry): Boolean {
            return ((oldItem.gameId == newItem.gameId)&&
                    (oldItem.gameName == newItem.gameName))
        }

        override fun areItemsTheSame(oldItem: GameListEntry, newItem: GameListEntry): Boolean {
            return (oldItem.gameId == newItem.gameId)
        }
    }

    // ViewHolder for each data item
    class GameListViewHolder(val cardView: CardView): RecyclerView.ViewHolder(cardView){
        internal fun bind(gameListEntry: GameListEntry){
            cardView.apply {
                game_list_entry_text.text = gameListEntry.gameName.plus(gameListEntry.gameId)
            }
        }
    }

    // Create new views (by layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_gamelist_view, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return GameListViewHolder(
            cardView
        )
    }

    override fun submitList(list: MutableList<GameListEntry>?) {
        super.submitList(list)
        entryList = list ?: mutableListOf<GameListEntry>()
    }

    // Replace contents of view (by layout manager)
    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        // - bind the result to the view
        holder.bind(getItem(position))
    }

    fun insertEntryOnDrag(itemPosToBeInserted: Int, itemPosHoveredOver: Int){
        val entryToBeShifted = entryList.removeAt(itemPosToBeInserted)
        notifyItemRemoved(itemPosToBeInserted)
        entryList.add(itemPosHoveredOver, entryToBeShifted)
        notifyItemInserted(itemPosHoveredOver)
    }

    fun removeEntryOnSwipe(position: Int){
        entryList.removeAt(position)
        notifyItemRemoved(position)
    }
}