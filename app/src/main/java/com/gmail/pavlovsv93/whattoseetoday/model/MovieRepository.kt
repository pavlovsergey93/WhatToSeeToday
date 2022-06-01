package com.gmail.pavlovsv93.whattoseetoday.model

class MovieRepository :  MovieInterfaceRepository{
    override fun getHomeMovies(): MutableList<Movie> {
        return getHomeMoviesList()
    }

    override fun getFavoritesMovies(): MutableList<Movie> {
        return getFavoritesMoviesList()
    }

    override fun getRatingMovies(): MutableList<Movie> {
        return getRatingMoviesList()
    }
}