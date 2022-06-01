package com.gmail.pavlovsv93.whattoseetoday.model.repo

import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.DetailsStarDTO
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.MovieDTO
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.MoviesListDTO
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.PopularPersonalDTO

interface MovieInterfaceRepository {

    fun getPopularMovies(callback: Callback<MutableList<Movie>>)
    fun getNewMovies(callback: Callback<MutableList<Movie>>)
    fun getRatingMovies(callback: Callback<MutableList<Movie>>)

    fun getCatalogMoviesRetrofit(catalog: String, lang: String = "ru-RU", page: Int = 1,callback: retrofit2.Callback<MoviesListDTO>)
    fun getMovieRetrofit(idMovie: Int, lang: String = "ru-RU", callback: retrofit2.Callback<MovieDTO>)
    fun findMoviesOnDB(findStr: String, lang: String = "ru-RU", page: Int = 1, includeAdult: Boolean, callback: retrofit2.Callback<MoviesListDTO>)

    fun getPopulStar(lang: String = "ru-RU",page: Int = 1, callback: retrofit2.Callback<PopularPersonalDTO>)
    fun getDetailsStar(uid: Int, lang: String = "ru-RU", callBackStar: retrofit2.Callback<DetailsStarDTO>)
}