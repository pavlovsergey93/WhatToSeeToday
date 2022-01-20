package com.gmail.pavlovsv93.whattoseetoday.model

class MovieRepository :  MovieInterfaceRepository{
    override fun getHomeMoviesList(): MutableList<Movie> {
        return getHomeMoviesList()
    }

    override fun getFavoritesMoviesList(): MutableList<Movie> {
        return getFavoritesMoviesList()
    }

    override fun getRatingMoviesList(): MutableList<Movie> {
        return getRatingMoviesList()
    }
}