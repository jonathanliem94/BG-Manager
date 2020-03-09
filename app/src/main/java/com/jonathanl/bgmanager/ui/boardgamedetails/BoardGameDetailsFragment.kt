package com.jonathanl.bgmanager.ui.boardgamedetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.data.models.BoardGameDetails
import com.jonathanl.bgmanager.databinding.FragmentGameDetailsBinding
import com.jonathanl.bgmanager.di.DaggerBoardGameDetailsComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
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
        disposable = subscribeToBoardGameDetails()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_game_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_category_option -> {
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
        return boardGameDetailsViewModel.getBoardGameDetails(args.gameId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onSuccess = {
                    initialiseGameImage(it.boardGameDetails)
                },
                onError = {
                    Log.e("BGameDetailsFragment", "subscribeToBoardGameDetails failed. $it")
                }
            )
    }

    private fun initialiseGameImage(boardGameDetails: BoardGameDetails) {
        Glide.with(binding.root)
            .load(boardGameDetails.bgImage)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_error)
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("BGameDetailsFragment", "(Glide) Image failed to load. $e")
                    binding.gameImage.visibility = View.GONE
                    initialiseGameData(boardGameDetails)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    initialiseGameData(boardGameDetails)
                    // to allow glide to update the target
                    return false
                }
            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.gameImage)
    }

    private fun initialiseGameData(boardGameDetails: BoardGameDetails) {
        //Log.d("BGameDetailsFragment", "initialiseGameData = $boardGameData")
        binding.run {
            gameNameText.text = args.gameName
            gameIdText.text = args.gameId
            gamePublishYearText.text = getString(R.string.bg_details_yearpublished, boardGameDetails.yearPublished)
            gameMinMaxPlayersText.text = getString(R.string.bg_details_minmaxplayers, boardGameDetails.minPlayers, boardGameDetails.maxPlayers)
            gameMinMaxPlayTimeText.text = getString(R.string.bg_details_minmaxplaytime, boardGameDetails.minPlayTime, boardGameDetails.maxPlayTime)
            gameDescriptionText.text = Html.fromHtml(boardGameDetails.description)
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