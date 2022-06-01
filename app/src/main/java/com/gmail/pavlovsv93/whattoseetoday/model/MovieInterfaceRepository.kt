package com.gmail.pavlovsv93.whattoseetoday.model

interface MovieInterfaceRepository {
    fun getHomeMovies(): MutableList<Movie>
    fun getFavoritesMovies(): MutableList<Movie>
    fun getRatingMovies(): MutableList<Movie>
}