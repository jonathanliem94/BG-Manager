package com.jonathanl.bgmanager.ui.boardgamedetailspage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoardGameDetailsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Game Details Fragment"
    }
    val text: LiveData<String> = _text

}