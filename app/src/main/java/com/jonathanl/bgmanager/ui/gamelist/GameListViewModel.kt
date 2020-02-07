package com.jonathanl.bgmanager.ui.gamelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is game list Fragment"
    }
    val text: LiveData<String> = _text
}