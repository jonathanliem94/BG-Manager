package com.jonathanl.bgmanager.ui.search

import android.view.View
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.jonathanl.bgmanager.ui.search.SearchFragmentDirections
import kotlinx.android.synthetic.main.recycler_search_view.view.*

class SearchViewOnClickListener: View.OnClickListener{
    override fun onClick(v: View?) {
        if (v == null) return
        val cardView = (v as CardView)
        val gameName = cardView.search_result_text.text.toString()
        val gameId = cardView.search_result_game_id.text.toString()
        val action = SearchFragmentDirections.actionNavSearchToNavGameDetails(gameName,gameId)
        cardView.findNavController().navigate(action)
    }

}