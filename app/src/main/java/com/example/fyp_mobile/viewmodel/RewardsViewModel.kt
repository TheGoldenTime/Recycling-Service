package com.example.fyp_mobile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fyp_mobile.model.Rewards
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RewardsViewModel(application: Application) : AndroidViewModel(application){

    val item = MutableLiveData<String>()

    val total = MutableLiveData<Int>()

    private val _data = MutableLiveData<List<Rewards>>(emptyList())
    val data: LiveData<List<Rewards>>
    get() = _data

    init {
        getAllRewards()
    }


    fun getAllRewards() {

    }
}