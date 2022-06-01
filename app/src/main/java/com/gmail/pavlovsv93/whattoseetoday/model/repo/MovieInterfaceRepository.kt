package com.gmail.pavlovsv93.whattoseetoday.model.repo

import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.MovieDTO
import com.gmail.pavlovsv93.whattoseetoday.model.MoviesListDTO

interface MovieInterfaceRepository {

    fun getPopularMovies(callback: Callback<MutableList<Movie>>)
    fun getNewMovies(callback: Callback<MutableList<Movie>>)
    fun getRatingMovies(callback: Callback<MutableList<Movie>>)

    fun getCatalogMoviesRetrofit(catalog: String, lang: String = "ru-RU", page: Int = 1,callback: retrofit2.Callback<MoviesListDTO>)
    fun getMovieRetrofit(idMovie: Int, lang: String = "ru-RU", callback: retrofit2.Callback<MovieDTO>)
}