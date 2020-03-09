package com.jonathanl.bgmanager.ui.gamelist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.NO_ID
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jonathanl.bgmanager.R
import com.jonathanl.bgmanager.base.BaseFragment
import com.jonathanl.bgmanager.databinding.FragmentGameListBinding
import com.jonathanl.bgmanager.di.DaggerGameListComponent
import com.jonathanl.bgmanager.ui.gamelist.dialogs.AddCategoryDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.ClassCastException
import javax.inject.Inject

class GameListFragment :
    BaseFragment(),
    GameListDragListener,
    ChipGroup.OnCheckedChangeListener,
    AddCategoryDialogFragment.AddCategoryDialogListener
{
    @Inject
    lateinit var gameListViewModel: GameListViewModel
    private var _binding: FragmentGameListBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var gameListAdapter: GameListViewAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpDI()
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView stuff
        gameListAdapter = GameListViewAdapter(this, gameListViewModel)
        val gestureCallback = GameListGestureCallback(gameListAdapter)
        itemTouchHelper = ItemTouchHelper(gestureCallback)
        binding.recyclerViewGameList.apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(this.context)
            // specify an viewAdapter
            adapter = gameListAdapter
        }
        // Attach gesture functionality (swipe and drag and drop) to recycler view
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewGameList)
        binding.gameListChipGroup.setOnCheckedChangeListener(this)
        initChipGroup()
        compositeDisposable.add(subscribeToGameListHolder())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_game_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_category_option -> {
                val dialog = AddCategoryDialogFragment(this)
                dialog.show(parentFragmentManager, "addCategory")
                true
            }
            R.id.filter_custom -> {
                //TODO: Add dialog to select and set multiple filters
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initChipGroup() {
        val chipSortNameAscending = createTemplateChip()
        chipSortNameAscending.text = getString(R.string.filter_name_ascending)
        val chipSortNameDescending = createTemplateChip()
        chipSortNameDescending.text = getString(R.string.filter_name_descending)
        binding.gameListChipGroup.run {
            addView(chipSortNameAscending)
            addView(chipSortNameDescending)
        }
    }

    private fun createAndAddCustomChip(filterText: String) {
        val newChip = createTemplateChip()
        newChip.text = filterText
        newChip.isCloseIconVisible = true
        newChip.setOnCloseIconClickListener {
            try {
                val chipGroup = it.parent as ChipGroup
                chipGroup.removeView(it)
            } catch (e: ClassCastException) {
                throw ClassCastException((context.toString() +
                        "Parent of chip is not ChipGroup???"))
            }
        }
        binding.gameListChipGroup.addView(newChip)
    }

    private fun createTemplateChip(): Chip {
        return layoutInflater.inflate(R.layout.chip_gamelist, binding.gameListChipGroup, false) as Chip
    }

    override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        if (checkedId != NO_ID) {
            val chipSelected = group?.findViewById<Chip>(checkedId)
            when (chipSelected?.text) {
                getString(R.string.filter_name_descending) -> gameListAdapter.sortGameListDescending()
                getString(R.string.filter_name_ascending) -> gameListAdapter.sortGameListAscending()
                //TODO: for custom chips, sort by that indicated tag
            }
        }
    }

    override fun onDialogPositiveClick(filterText: String) {
        createAndAddCustomChip(filterText)
    }

    private fun subscribeToGameListHolder(): Disposable{
        return gameListViewModel.gameListHolder
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    (binding.recyclerViewGameList.adapter as GameListViewAdapter).submitList(it)
                },
                onError = {
                    Log.e("GameListFragment", "subscribeToGameListObservable failed. $it")
                }
            )
    }

    private fun setUpDI() {
        DaggerGameListComponent.builder()
            .mainActivityComponent(getMainActivityComponent())
            .build()
            .inject(this)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onDestroyView() {
        gameListAdapter.saveListToDb()
        compositeDisposable.dispose()
        _binding = null
        super.onDestroyView()
    }
}

interface GameListDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

}