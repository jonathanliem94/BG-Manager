package com.jonathanl.bgmanager.ui.search.recyclerview

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.network.BoardGameResult
import kotlinx.android.synthetic.main.recycler_search_view.view.*

class SearchViewAdapter:
    ListAdapter<BoardGameResult, SearchViewAdapter.SearchViewHolder>(BoardGameResultDiffCallback()) {

    // Provide itemcallback to allow DiffUtil to determine what is different from the old and new items
    class BoardGameResultDiffCallback : DiffUtil.ItemCallback<BoardGameResult>() {
        override fun areContentsTheSame(oldItem: BoardGameResult, newItem: BoardGameResult): Boolean {
            return ((oldItem.gameId == newItem.gameId)&&
                    (oldItem.boardGameNameResult.gameName == newItem.boardGameNameResult.gameName))
        }

        override fun areItemsTheSame(oldItem: BoardGameResult, newItem: BoardGameResult): Boolean {
            return (oldItem.gameId == newItem.gameId)
        }
    }

    // Create new views (by layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_search_view, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return SearchViewHolder(cardView)

    }

    // Replace contents of view (by layout manager)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        // - bind the result to the view
        holder.bind(getItem(position))
    }

    // ViewHolder for each data item
    class SearchViewHolder(val cardView: CardView): RecyclerView.ViewHolder(cardView){
        internal fun bind(boardGameResult: BoardGameResult){
            val activity = cardView.context as Activity
            cardView.apply {
                search_result_image.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.ic_launcher_background))
                search_result_text.text = boardGameResult.boardGameNameResult.gameName
                search_result_game_id.text = boardGameResult.gameId
                setOnClickListener(SearchViewOnClickListener())
            }
        }
    }

}