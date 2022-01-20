package com.gmail.pavlovsv93.whattoseetoday.model

interface MovieInterfaceRepository {
    fun getHomeMoviesList(): MutableList<Movie>
    fun getFavoritesMoviesList(): MutableList<Movie>
    fun getRatingMoviesList(): MutableList<Movie>
}