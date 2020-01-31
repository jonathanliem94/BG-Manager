package com.jonathanl.bgmanager.ui.boardgamedetailspage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jonathanl.bgmanager.R

class BoardGameDetailsFragment : Fragment() {

    private val boardGameDetailsViewModel: BoardGameDetailsViewModel by viewModels()
    private val args: BoardGameDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_game_details, container, false)
        val gameNameTextView: TextView = root.findViewById(R.id.text_gamename)
        val gameIdTextView: TextView = root.findViewById(R.id.text_gameid)
        gameNameTextView.text = args.gameName
        gameIdTextView.text = args.gameId
        return root
    }
}