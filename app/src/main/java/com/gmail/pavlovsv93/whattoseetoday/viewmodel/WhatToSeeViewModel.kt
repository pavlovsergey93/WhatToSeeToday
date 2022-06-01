package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pavlovsv93.whattoseetoday.model.AppState
import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.*
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.db.AppDB
import com.gmail.pavlovsv93.whattoseetoday.model.repo.*
import retrofit2.Call
import retrofit2.Response

private const val SERVER_ERROR = "Ошибка сервера"
private const val PROJECT_ERROR = "Ошибка приложения"
private const val CORRUPTED_DATA = "Потеря данных"

internal class WhatToSeeViewModel(
    private val livaDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repo: MovieInterfaceRepository = MovieRepository(RemoteDataSource())
) : ViewModel(), InterfaceViewModel {

    private val repoJournal: RoomInterfaceJournalRepository =
        RoomJournalRepository(AppDB.getJournalDAO())
    private val repoRoom: RoomInterfaceRepository = RoomRepository(AppDB.getMoviesDAO())

    override fun getData(): LiveData<AppState> = livaDataToObserver

    //Из урока получения данных с сервера
    //-------------------------------------------------------------------------
    override fun getPopularMovies() {
        livaDataToObserver.value = AppState.OnLoading
        repo.getPopularMovies(callback = object : Callback<MutableList<Movie>> {
            override fun onSuccess(result: MutableList<Movie>) {
                livaDataToObserver.postValue(AppState.OnSuccess(result))
            }

            override fun onError(exception: Throwable) {
                livaDataToObserver.postValue(AppState.OnError(exception))
            }
        })
    }

    override fun getNewMovies() {
        livaDataToObserver.value = AppState.OnLoading
        repo.getNewMovies(callback = object : Callback<MutableList<Movie>> {
            override fun onSuccess(result: MutableList<Movie>) {
                livaDataToObserver.postValue(AppState.OnSuccess(result))
            }

            override fun onError(exception: Throwable) {
                livaDataToObserver.postValue(AppState.OnError(exception))
            }
        })
    }

    override fun getRatingMovies() {
        livaDataToObserver.value = AppState.OnLoading
        repo.getRatingMovies(callback = object : Callback<MutableList<Movie>> {
            override fun onSuccess(result: MutableList<Movie>) {
                livaDataToObserver.postValue(AppState.OnSuccess(result))
            }

            override fun onError(exception: Throwable) {
                livaDataToObserver.postValue(AppState.OnError(exception))
            }
        })
    }

    //-------------------------------------------------------------------------

    // Получение данных Retrofit
    private val callBackMovie = object : retrofit2.Callback<MovieDTO> {
        override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
            val serverResponse: MovieDTO? = response.body()
            livaDataToObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkMovieResponse(serverResponse)
                } else {
                    AppState.OnError(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<MovieDTO>, t: Throwable) {
            AppState.OnError(Throwable(PROJECT_ERROR))
        }
    }

    private fun checkMovieResponse(serverResponse: MovieDTO): AppState {
        return if (serverResponse == null) {
            AppState.OnError(Throwable(CORRUPTED_DATA))
        } else {
            AppState.OnSuccess(convertData(serverResponse))
        }
    }

    private fun convertData(serverResponse: MovieDTO): MutableList<Movie> {
        return mutableListOf(
            Movie(
                id = serverResponse.id,
                name = serverResponse.title,
                description = serverResponse.overview,
                poster = serverResponse.poster_path,
                rating = serverResponse.vote_average,
                date = serverResponse.release_date
            )
        )
    }

    private val callBackCatalog = object : retrofit2.Callback<MoviesListDTO> {
        override fun onResponse(call: Call<MoviesListDTO>, response: Response<MoviesListDTO>) {
            val serverResponse: MoviesListDTO? = response.body()
            livaDataToObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkCatalogResponse(serverResponse)
                } else {
                    AppState.OnError(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<MoviesListDTO>, t: Throwable) {
            AppState.OnError(Throwable(PROJECT_ERROR))
        }

    }

    private fun checkCatalogResponse(serverResponse: MoviesListDTO): AppState {
        return AppState.OnSuccess(convertCatalogData(serverResponse))
    }

    private fun convertCatalogData(serverResponse: MoviesListDTO): MutableList<Movie> {
        val moviesList: MutableList<Movie> = mutableListOf()
        with(serverResponse) {
            for (i in results.indices) {
                moviesList.add(
                    Movie(
                        id = results[i].id,
                        name = results[i].title,
                        description = results[i].overview,
                        poster = results[i].poster_path,
                        rating = results[i].vote_average,
                        date = results[i].release_date
                    )
                )
            }
        }
        return moviesList
    }


    fun getCatalogMoviesRetrofit(catalog: String, page: Int = 1) {
        livaDataToObserver.value = AppState.OnLoading
        repo.getCatalogMoviesRetrofit(catalog = catalog, page = page, callback = callBackCatalog)
    }

    fun getMovieRetrofit(idMovie: Int) {
        livaDataToObserver.value = AppState.OnLoading
        repo.getMovieRetrofit(idMovie = idMovie, callback = callBackMovie)
    }


    override fun findMoviesOnDB(findStr: String, includeAdult: Boolean) {
        livaDataToObserver.value = AppState.OnLoading
        repo.findMoviesOnDB(findStr = findStr, callback = callBackCatalog, includeAdult = includeAdult)
    }
    //----------------------------------------------------------------------------------------------

    override fun getMoviesDB() {
        repoRoom.getAllLocalMovies()
    }

    override fun setMovieInFavorite(movie: Movie) {
        repoRoom.setItemInDB(movie)
    }

    override fun delMovieOnFavorite(idMovie: Int) {
        repoRoom.delItemToTheDB(idMovie = idMovie)
    }

    override fun getMoviesFavorite() {
        livaDataToObserver.value = AppState.OnLoading
        repoRoom.getLocalAll(object : Callback<MutableList<Movie>> {
            override fun onSuccess(result: MutableList<Movie>) {
                livaDataToObserver.postValue(AppState.OnSuccess(result))
            }

            override fun onError(exception: Throwable) {
                livaDataToObserver.postValue(AppState.OnError(exception))
            }

        })
    }

    override fun getJournal() {
        livaDataToObserver.value = AppState.OnLoading
        repoJournal.getLocalAllJournal(object : Callback<MutableList<Movie>> {
            override fun onSuccess(result: MutableList<Movie>) {
                livaDataToObserver.postValue(AppState.OnSuccess(result))
            }

            override fun onError(exception: Throwable) {
                livaDataToObserver.postValue(AppState.OnError(exception))
            }
        })
    }

    override fun setMovieInJournal(movie: Movie) {
        repoJournal.setItemInDBJournal(movie)
    }

    override fun delMovieOnJournal(idMovie: Int) {
        repoJournal.delItemToTheDBJournal(idMovie)
    }

    fun findItemInJournal(idMovie: Int): Boolean = repoJournal.findItem(idMovie)

    fun findItemInMovieDB(idMovie: Int): Boolean = repoRoom.findItem(idMovie)

}




