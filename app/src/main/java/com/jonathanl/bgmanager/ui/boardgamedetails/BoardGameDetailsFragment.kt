package com.jonathanl.bgmanager.ui.boardgamedetails

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.data.models.BoardGameData
import com.jonathanl.bgmanager.di.DaggerBoardGameDetailsComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class BoardGameDetailsFragment : BaseFragment() {

    @Inject
    lateinit var boardGameDetailsViewModel: BoardGameDetailsViewModel
    private val args: BoardGameDetailsFragmentArgs by navArgs()
    lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
        val root = inflater.inflate(R.layout.fragment_game_details, container, false)
        disposable = subscribeToBoardGameDetails()
        initialiseUI(root)
        boardGameDetailsViewModel.getBoardGameDetails(args.gameId)
        return root
    }

    private fun subscribeToBoardGameDetails(): Disposable {
        return boardGameDetailsViewModel.boardGameDetails
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    initialiseGameData(it.boardGameData)
                },
                onError = {
                    Log.e("BGameDetailsFragment", "subscribeToBoardGameDetails failed. $it")
                }
            )
    }

    private fun initialiseGameData(boardGameData: BoardGameData) {
        Log.d("BGameDetailsFragment", "initialiseGameData = $boardGameData")
        view?.run {
            findViewById<TextView>(R.id.gamePublishYearText).text = getString(R.string.bg_details_yearpublished, boardGameData.yearPublished)
            findViewById<TextView>(R.id.gameMinMaxPlayersText).text = getString(R.string.bg_details_minmaxplayers, boardGameData.minPlayers, boardGameData.maxPlayers)
            findViewById<TextView>(R.id.gameMinMaxPlayTimeText).text = getString(R.string.bg_details_minmaxplaytime, boardGameData.minPlayTime, boardGameData.maxPlayTime)
            findViewById<TextView>(R.id.gameDescriptionText).text = Html.fromHtml(boardGameData.description)
        }
    }

    private fun initialiseUI(root: View) {
        val gameNameTextView: TextView = root.findViewById(R.id.gameNameText)
        val gameIdTextView: TextView = root.findViewById(R.id.gameIdText)
        gameNameTextView.text = args.gameName
        gameIdTextView.text = args.gameId
    }

    private fun setUpDI() {
        DaggerBoardGameDetailsComponent.builder()
            .mainActivityComponent(getMainActivityComponent())
            .build()
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}