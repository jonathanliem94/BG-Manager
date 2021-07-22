package com.jonathanl.bgmanager.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.data.models.BoardGameSearchId

class SearchViewAdapter(private val searchViewModel: SearchViewModel):
    ListAdapter<BoardGameSearchId, SearchViewAdapter.SearchViewHolder>(BoardGameResultDiffCallback()) {

    // Provide itemcallback to allow DiffUtil to determine what is different from the old and new items
    class BoardGameResultDiffCallback : DiffUtil.ItemCallback<BoardGameSearchId>() {
        override fun areContentsTheSame(oldItem: BoardGameSearchId, newItem: BoardGameSearchId): Boolean {
            return ((oldItem.gameId == newItem.gameId)&&
                    (oldItem.boardGameSearchNameResult.gameName == newItem.boardGameSearchNameResult.gameName))
        }

        override fun areItemsTheSame(oldItem: BoardGameSearchId, newItem: BoardGameSearchId): Boolean {
            return (oldItem.gameId == newItem.gameId)
        }
    }

    // ViewHolder for each data item
    class SearchViewHolder(private val cardView: CardView): RecyclerView.ViewHolder(cardView){
        internal fun bind(boardGameSearchId: BoardGameSearchId, searchViewModel: SearchViewModel){

            val searchResultImage = cardView.findViewById<ImageView>(R.id.search_result_image)
            val searchResultText = cardView.findViewById<TextView>(R.id.search_result_text)
            val searchResultGameId = cardView.findViewById<TextView>(R.id.search_result_game_id)
            val searchResultDetails = cardView.findViewById<ImageView>(R.id.search_result_gotodetails_img)
            val searchResultAddList = cardView.findViewById<ImageView>(R.id.search_result_addtogamelist_img)

            searchResultImage.setImageDrawable(AppCompatResources.getDrawable(cardView.context, R.drawable.ic_launcher_background))
            searchResultText.text = boardGameSearchId.boardGameSearchNameResult.gameName
            searchResultGameId.text = boardGameSearchId.gameId
            searchResultDetails.setOnClickListener(SearchViewOnClickMoreDetails())
            searchResultAddList.setOnClickListener(SearchViewOnClickAddToGameList(searchViewModel))
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
        holder.bind(getItem(position), searchViewModel)
        setAppearAnimation(holder.itemView)
    }

    private fun setAppearAnimation(viewToAnimate: View) {
        if (viewToAnimate.animation == null) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.fade_in)
            viewToAnimate.animation = animation
        }
    }

}