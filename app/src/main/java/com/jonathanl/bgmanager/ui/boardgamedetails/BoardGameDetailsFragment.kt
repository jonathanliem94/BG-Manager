package com.jonathanl.bgmanager.ui.boardgamedetails

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.data.models.BoardGameData
import com.jonathanl.bgmanager.databinding.FragmentGameDetailsBinding
import com.jonathanl.bgmanager.di.DaggerBoardGameDetailsComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class BoardGameDetailsFragment : BaseFragment() {

    @Inject
    lateinit var boardGameDetailsViewModel: BoardGameDetailsViewModel
    private val args: BoardGameDetailsFragmentArgs by navArgs()
    private var _binding: FragmentGameDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        initialiseUI()
        return binding.root
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
        binding.run {
            gamePublishYearText.text = getString(R.string.bg_details_yearpublished, boardGameData.yearPublished)
            gameMinMaxPlayersText.text = getString(R.string.bg_details_minmaxplayers, boardGameData.minPlayers, boardGameData.maxPlayers)
            gameMinMaxPlayTimeText.text = getString(R.string.bg_details_minmaxplaytime, boardGameData.minPlayTime, boardGameData.maxPlayTime)
            gameDescriptionText.text = Html.fromHtml(boardGameData.description)
        }
    }

    private fun initialiseUI() {
        disposable = subscribeToBoardGameDetails()
        boardGameDetailsViewModel.getBoardGameDetails(args.gameId)
        binding.run {
            gameNameText.text = args.gameName
            gameIdText.text = args.gameId
        }
    }

    private fun setUpDI() {
        DaggerBoardGameDetailsComponent.builder()
            .mainActivityComponent(getMainActivityComponent())
            .build()
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        disposable.dispose()
    }
}