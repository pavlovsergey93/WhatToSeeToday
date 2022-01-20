package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.MovieInterfaceRepository
import com.gmail.pavlovsv93.whattoseetoday.model.MovieRepository
import java.lang.Thread.sleep

internal class WhatToSeeHomeViewModel(private val livaDataToObserver : MutableLiveData<AppState> = MutableLiveData()) : ViewModel(), InterfaceViewModel{

    private val repo : MovieInterfaceRepository = MovieRepository()

    private val moviesListHome : MutableList<Movie>? = null

    override fun getData() : LiveData<AppState> {
        getDataFromDB()
        return livaDataToObserver
    }

    override fun getDataFromDB(){
        Thread{
            livaDataToObserver.postValue(AppState.OnLoading)
            val random = (0..1).shuffled().last()
            sleep(4000)
            if (random == 1){
                livaDataToObserver.postValue(AppState.OnSuccess(moviesListHome))
            }
            else{
                livaDataToObserver.postValue(AppState.OnError(Throwable("Произошла ошибка")))
            }
        }.start()
    }

}




