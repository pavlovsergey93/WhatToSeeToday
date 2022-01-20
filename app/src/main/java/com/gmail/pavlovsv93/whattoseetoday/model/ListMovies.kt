package com.gmail.pavlovsv93.whattoseetoday.model

data class ListMovies(val moviesList: MutableList<Movie> = getHomeMoviesList() )

fun getHomeMoviesList() : MutableList<Movie> = mutableListOf(
    Movie(name = "1", description = "1", poster = null),
    Movie(name = "2", description = "2", poster = null),
    Movie(name = "3", description = "3", poster = null),
    Movie(name = "4", description = "4", poster = null),
    Movie(name = "5", description = "5", poster = null),
    Movie(name = "6", description = "6", poster = null)
)
fun getFavoritesMoviesList() : MutableList<Movie> = mutableListOf(
    Movie(name = "7", description = "1", poster = null),
    Movie(name = "8", description = "2", poster = null),
    Movie(name = "9", description = "3", poster = null),
    Movie(name = "10", description = "4", poster = null),
    Movie(name = "11", description = "5", poster = null),
    Movie(name = "12", description = "6", poster = null)
)
fun getRatingMoviesList() : MutableList<Movie> = mutableListOf(
    Movie(name = "13", description = "1", poster = null),
    Movie(name = "14", description = "2", poster = null),
    Movie(name = "15", description = "3", poster = null),
    Movie(name = "16", description = "4", poster = null),
    Movie(name = "17", description = "5", poster = null),
    Movie(name = "18", description = "6", poster = null)
)
