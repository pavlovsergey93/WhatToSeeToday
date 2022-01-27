package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData

interface InterfaceViewModel {
    fun getData() : LiveData<AppState>

    fun getPopularMovies()
}