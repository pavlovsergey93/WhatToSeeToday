package com.gmail.pavlovsv93.whattoseetoday.model

interface MovieInterfaceRepository {

    fun getPopularMovies(callback: Callback<MutableList<Movie>>)


}