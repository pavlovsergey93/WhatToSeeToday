package com.gmail.pavlovsv93.whattoseetoday.model.DTO

import com.gmail.pavlovsv93.whattoseetoday.BuildConfig
import com.gmail.pavlovsv93.whattoseetoday.model.repo.MovieDatabaseAPI
import com.gmail.pavlovsv93.whattoseetoday.view.BASE_URL_RETROFIT
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    private val movieAPI = Retrofit.Builder().baseUrl(BASE_URL_RETROFIT)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        ).build()
        .create(MovieDatabaseAPI::class.java)

    fun getMovieDetailsRetrofit(idMovie: Int, lang: String,callback: Callback<MovieDTO>) {
        movieAPI.getDetailsMovie(
            idMovie = idMovie,
            apiKey = BuildConfig.TMDB_API_KEY,
            lang = lang
        ).enqueue(callback)
    }

    fun getMoviesCatalogRetrofit(
        catalog: String,
        lang: String,
        page: Int,
        callback: Callback<MoviesListDTO>
    ) {
        movieAPI.getMoviesCatalog(
            catalog = catalog,
            apiKey = BuildConfig.TMDB_API_KEY,
            lang = lang,
            page = page
        ).enqueue(callback)
    }
}
