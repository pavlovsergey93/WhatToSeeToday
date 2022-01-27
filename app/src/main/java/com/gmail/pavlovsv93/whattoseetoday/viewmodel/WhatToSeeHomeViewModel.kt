package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.MovieInterfaceRepository
import com.gmail.pavlovsv93.whattoseetoday.model.MovieRepository
import java.lang.Thread.sleep

internal class WhatToSeeHomeViewModel(private val livaDataToObserver: MutableLiveData<AppState> = MutableLiveData())
    : ViewModel(), InterfaceViewModel {

    private val repo: MovieInterfaceRepository = MovieRepository()

    private val moviesListHome: MutableList<Movie>? = null

    override fun getData(): LiveData<AppState> = livaDataToObserver

    override fun getPopularMovies() {
        livaDataToObserver.value = AppState.OnLoading
        repo.getPopularMovies(callback = object : Callback<MutableList<Movie>>{
            override fun onSuccess(result: MutableList<Movie>) {
                livaDataToObserver.postValue(AppState.OnSuccess(result))
            }

            override fun onError(exception: Throwable) {
                livaDataToObserver.postValue(AppState.OnError(exception))
            }
        })
    }
}




