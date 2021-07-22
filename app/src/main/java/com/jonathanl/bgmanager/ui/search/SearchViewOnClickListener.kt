package com.jonathanl.bgmanager.ui.search

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.data.models.GameListEntry

interface SearchViewOnClickListener: View.OnClickListener

class SearchViewOnClickMoreDetails: SearchViewOnClickListener{
    override fun onClick(v: View?) {
        val toast = Toast.makeText(v?.context, "Navigating to game details", Toast.LENGTH_SHORT)
        toast.show()
        when {
            v == null -> {
                return
            }
            v.parent.parent is CardView -> {
                (v.parent.parent as CardView).apply {
                    val gameName = this.findViewById<TextView>(R.id.search_result_text).text.toString()
                    val gameId = this.findViewById<TextView>(R.id.search_result_game_id).text.toString()
                    val action = SearchFragmentDirections.actionNavSearchToNavGameDetails(gameName,gameId)
                    findNavController().navigate(action)
                }
            }
        }
    }
}

class SearchViewOnClickAddToGameList(private val searchViewModel: SearchViewModel): SearchViewOnClickListener{
    override fun onClick(v: View?) {
        val toast = Toast.makeText(v?.context, "Added to game list", Toast.LENGTH_SHORT)
        toast.show()
        when {
            v == null -> {
                return
            }
            v.parent.parent is CardView -> {
                (v.parent.parent as CardView).apply {
                    val newGameListEntry =
                        GameListEntry(
                            this.findViewById<TextView>(R.id.search_result_game_id).text.toString(),
                            this.findViewById<TextView>(R.id.search_result_text).text.toString()
                        )
                    searchViewModel.addNewEntryToGameList(newGameListEntry)
                }
            }
        }
    }
}