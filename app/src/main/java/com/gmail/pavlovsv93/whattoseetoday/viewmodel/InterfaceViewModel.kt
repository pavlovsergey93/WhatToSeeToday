package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import com.gmail.pavlovsv93.whattoseetoday.model.AppState
import com.gmail.pavlovsv93.whattoseetoday.model.Movie

interface InterfaceViewModel {
    fun getData(): LiveData<AppState>

    fun getPopularMovies()

    fun getNewMovies()

    fun getRatingMovies()

    fun getMoviesFavorite()

    fun setMovieInFavorite(movie: Movie)

    fun delMovieOnFavorite(idMovie: Int)

    fun getMoviesDB()
}