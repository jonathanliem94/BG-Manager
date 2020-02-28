package com.jonathanl.bgmanager.ui.boardgamedetails

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.data.models.BoardGameData
import com.jonathanl.bgmanager.databinding.FragmentGameDetailsBinding
import com.jonathanl.bgmanager.di.DaggerBoardGameDetailsComponent
import com.squareup.picasso.Picasso
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
        setHasOptionsMenu(true)
        setUpDI()
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        initialiseUI()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_game_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_game_option -> {
                binding.run {
                    boardGameDetailsViewModel.addToGameList(
                        gameNameText.text.toString(),
                        gameIdText.text.toString()
                    )
                    val toast = Toast.makeText(view?.context, "Added to game list", Toast.LENGTH_SHORT)
                    toast.show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        //Log.d("BGameDetailsFragment", "initialiseGameData = $boardGameData")
        binding.run {
            Picasso.get()
                .load(boardGameData.bgImage)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error)
                .into(gameImage)
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