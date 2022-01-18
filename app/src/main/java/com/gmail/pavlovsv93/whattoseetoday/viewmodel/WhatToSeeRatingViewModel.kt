package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.Movie

class WhatToSeeRatingViewModel(private val livaDataToObserver : MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), InterfaceViewModel {

    private val moviesListRating : MutableList<Movie>? = null

    override fun getData(): LiveData<AppState> {
        getDataFromDB()
        return livaDataToObserver
    }

    override fun getDataFromDB() {
        Thread{
            Thread.sleep(4000)
            livaDataToObserver.postValue(AppState.OnSuccess(moviesListRating))
        }.start()
    }
}