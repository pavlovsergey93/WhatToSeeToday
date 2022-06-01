package com.gmail.pavlovsv93.whattoseetoday.model

interface MovieInterfaceRepository {

    fun getPopularMovies(callback: Callback<MutableList<Movie>>)
    fun getNewMovies(callback: Callback<MutableList<Movie>>)
    fun getRatingMovies(callback: Callback<MutableList<Movie>>)
}