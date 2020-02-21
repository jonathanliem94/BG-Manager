package com.jonathanl.bgmanager.ui.boardgamedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.di.DaggerBoardGameDetailsComponent
import javax.inject.Inject

class BoardGameDetailsFragment : BaseFragment() {

    @Inject
    lateinit var boardGameDetailsViewModel: BoardGameDetailsViewModel
    private val args: BoardGameDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
        val root = inflater.inflate(R.layout.fragment_game_details, container, false)
        val gameNameTextView: TextView = root.findViewById(R.id.text_gamename)
        val gameIdTextView: TextView = root.findViewById(R.id.text_gameid)
        gameNameTextView.text = args.gameName
        gameIdTextView.text = args.gameId
        boardGameDetailsViewModel.getBoardGameDetails(args.gameId)
        return root
    }

    private fun setUpDI() {
        DaggerBoardGameDetailsComponent.builder()
            .mainActivityComponent(getMainActivityComponent())
            .build()
            .inject(this)
    }
}