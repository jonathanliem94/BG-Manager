package com.jonathanl.bgmanager.ui.gamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jonathanl.bgmanager.R

class GameListFragment : Fragment() {

    private lateinit var gameListViewModel: GameListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gameListViewModel =
            ViewModelProviders.of(this).get(GameListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_game_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_gamelist)
        gameListViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}