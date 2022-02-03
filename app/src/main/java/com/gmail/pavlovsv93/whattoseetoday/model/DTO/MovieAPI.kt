package com.gmail.pavlovsv93.whattoseetoday.model.repo

import com.gmail.pavlovsv93.whattoseetoday.model.DTO.MovieDTO
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.MoviesListDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDatabaseAPI {
    @GET("3/movie/{catalog}?")
    fun getMoviesCatalog(
        @Path("catalog") catalog: String,
        @Query("api_key") apiKey: String,
        @Query("language") lang: String,
        @Query("page") page: Int
    ): Call<MoviesListDTO>

    @GET("3/movie/{movie_id}?")
    fun getDetailsMovie(
        @Path("movie_id") idMovie: Int,
        @Query("api_key") apiKey: String,
        @Query("language") lang: String
    ): Call<MovieDTO>
}