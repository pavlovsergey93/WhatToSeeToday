package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import com.gmail.pavlovsv93.whattoseetoday.model.Movie

sealed class AppState{
    data class OnSuccess(val moviesData: MutableList<Movie>?) : AppState()
    data class OnError(val exception: Throwable) : AppState()
    object OnLoading : AppState()
}
