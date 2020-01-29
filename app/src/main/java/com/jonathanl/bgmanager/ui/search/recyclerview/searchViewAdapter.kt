package com.jonathanl.bgmanager.ui.search.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.network.BoardGameResult
import kotlinx.android.synthetic.main.search_view.view.*

class SearchViewAdapter(private var inputData: List<BoardGameResult>): RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder>() {

    // reference for each data item
    class SearchViewHolder(val cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return inputData.size
    }

    // Create new views (by layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.search_view, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters
        return SearchViewHolder(cardView)

    }

    // Replace contents of view (by layout manager)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardView.search_result_text.text = inputData.elementAt(position).boardGameNameResult.gameName
        holder.cardView.search_result_image.setImageResource(R.drawable.ic_launcher_background)
    }

    fun setInputData(newData: List<BoardGameResult>) {
        inputData = newData
        notifyDataSetChanged()
    }

}