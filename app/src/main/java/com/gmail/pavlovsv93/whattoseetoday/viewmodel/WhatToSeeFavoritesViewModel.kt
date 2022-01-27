package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.Movie

class WhatToSeeFavoritesViewModel(private val livaDataToObserver: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), InterfaceViewModel {

    private val moviesListFavorites : MutableList<Movie>? = null

    override fun getData(): LiveData<AppState> {
        getPopularMovies()
        return livaDataToObserver
    }

    override fun getPopularMovies() {
        Thread{
            Thread.sleep(4000)
            //livaDataToObserver.postValue(AppState.OnSuccess(moviesListFavorites))
        }.start()
    }
}