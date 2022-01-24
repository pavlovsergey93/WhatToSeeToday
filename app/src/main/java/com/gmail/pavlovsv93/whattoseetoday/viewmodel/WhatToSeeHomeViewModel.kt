package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.MovieInterfaceRepository
import com.gmail.pavlovsv93.whattoseetoday.model.MovieRepository
import java.lang.Thread.sleep

internal class WhatToSeeHomeViewModel(private val livaDataToObserver: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel(), InterfaceViewModel {

    private val repo: MovieInterfaceRepository = MovieRepository()

    private val moviesListHome: MutableList<Movie>? = null

    override fun getData(): LiveData<AppState> = livaDataToObserver

    override fun getDataFromDB() {
        livaDataToObserver.value = AppState.OnLoading
        Thread {
            sleep(2000)
            val random = (0..1).shuffled().last()
            if (random == 1) {
                livaDataToObserver.postValue(AppState.OnSuccess(repo.getHomeMovies()))
            } else {
                livaDataToObserver.postValue(AppState.OnError(Throwable("Произошла ошибка")))
            }
        }.start()
    }

}




