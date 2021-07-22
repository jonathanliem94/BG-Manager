package com.jonathanl.bgmanager.ui.gamelist

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.data.models.GameListEntry

class GameListViewAdapter(
    private val gameListDragListener: GameListDragListener,
    private val gameListViewModel: GameListViewModel
    ):
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
    class GameListViewHolder(private val cardView: CardView): RecyclerView.ViewHolder(cardView){
        internal fun bind(gameListEntry: GameListEntry, gameListDragListener: GameListDragListener){

            val gameListEntryText = cardView.findViewById<TextView>(R.id.gameListEntryText)
            val reorderImage = cardView.findViewById<ImageView>(R.id.reorderImage)

            gameListEntryText.text = gameListEntry.gameName.plus(gameListEntry.gameId)
            reorderImage.setOnTouchListener { _, event ->
                if (event?.actionMasked == MotionEvent.ACTION_DOWN) {
                    gameListDragListener.onStartDrag(this@GameListViewHolder)
                }
                true
            }
        }
    }

    // Create new views (by layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_gamelist_view, parent, false) as CardView
        return GameListViewHolder(cardView)
    }

    override fun submitList(list: MutableList<GameListEntry>?) {
        super.submitList(list)
        entryList = list ?: mutableListOf()
    }

    // Replace contents of view (by layout manager)
    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        // - bind the result to the view
        holder.bind(getItem(position), gameListDragListener)
    }

    fun saveListToDb() {
        gameListViewModel.saveGameListToDb(entryList)
    }

    fun insertEntryOnDrag(itemFromPos: Int, itemToPos: Int){
        val entryToBeShifted = entryList.removeAt(itemFromPos)
        entryList.add(itemToPos, entryToBeShifted)
        notifyItemMoved(itemFromPos, itemToPos)
        saveListToDb()
    }

    fun removeEntryOnSwipe(position: Int){
        val removedItem = entryList.removeAt(position)
        notifyItemRemoved(position)
        gameListViewModel.removeGameEntryFromDb(removedItem)
    }

    fun sortGameListAscending() {
        entryList.sortBy {
            it.gameName
        }
        notifyDataSetChanged()
    }

    fun sortGameListDescending() {
        entryList.sortByDescending {
            it.gameName
        }
        notifyDataSetChanged()
    }
}