package com.gmail.pavlovsv93.whattoseetoday.model.repo

import android.os.Build
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.*
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.view.API_KEY
import com.gmail.pavlovsv93.whattoseetoday.view.BASE_URL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MovieRepository(private val remoteDataSource: RemoteDataSource) : MovieInterfaceRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var urlConnection: HttpsURLConnection

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getPopularMovies(callback: Callback<MutableList<Movie>>) {
        executor.execute {
            try {
                val convertBufferToString: String =
                    getData("https://api.themoviedb.org/3/movie/popular")
                val jsonObject = JSONObject(convertBufferToString)
                val typeToken = object : TypeToken<List<MovieDTO>>() {}.type

                val moviesListDTO =
                    Gson().fromJson<List<MovieDTO>>(jsonObject.getString("results"), typeToken)

                val moviesList: MutableList<Movie> = purrs(moviesListDTO)
                handler.post {
                    callback.onSuccess(moviesList)
                }
            } catch (exc: Exception) {
                Log.e("", "Fail connection", exc)
                exc.printStackTrace()
                handler.post {
                    callback.onError(exc)
                }
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getNewMovies(callback: Callback<MutableList<Movie>>) {
        executor.execute {
            try {
                val jsonObject = JSONObject(getData("https://api.themoviedb.org/3/movie/now_playing"))
                val typeToken = object : TypeToken<List<MovieDTO>>() {}.type
                val moviesListDTO =
                    Gson().fromJson<List<MovieDTO>>(jsonObject.getString("results"), typeToken)
                val moviesList: MutableList<Movie> = purrs(moviesListDTO)
                handler.post {
                    callback.onSuccess(moviesList)
                }
            } catch (exc: Exception) {
                Log.e("", "Fail connection", exc)
                exc.printStackTrace()
                handler.post {
                    callback.onError(exc)
                }
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getRatingMovies(callback: Callback<MutableList<Movie>>) {
        executor.execute {
            try {
                val jsonObject = JSONObject(getData("https://api.themoviedb.org/3/movie/top_rated"))
                val typeToken = object : TypeToken<List<MovieDTO>>() {}.type
                val moviesListDTO =
                    Gson().fromJson<List<MovieDTO>>(jsonObject.getString("results"), typeToken)
                val moviesList: MutableList<Movie> = purrs(moviesListDTO)
                handler.post {
                    callback.onSuccess(moviesList)
                }
            } catch (exc: Exception) {
                Log.e("", "Fail connection", exc)
                exc.printStackTrace()
                handler.post {
                    callback.onError(exc)
                }
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    override fun getCatalogMoviesRetrofit(catalog: String, lang: String, page: Int , callback: retrofit2.Callback<MoviesListDTO>) {
        remoteDataSource.getMoviesCatalogRetrofit(catalog = catalog, lang = lang, page = page , callback = callback)
    }

    override fun getMovieRetrofit(idMovie: Int, lang: String, callback: retrofit2.Callback<MovieDTO>) {
        remoteDataSource.getMovieDetailsRetrofit(idMovie = idMovie, lang = lang, callback = callback)
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun getData(uri: String, page: Int = 1): String {

        val uri = URL(uri + "?api_key=${API_KEY}&language=ru-RU&page=" + page)
        urlConnection = uri.openConnection() as HttpsURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.readTimeout = 10000
        val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

    private fun purrs(moviesListDTO: List<MovieDTO>): MutableList<Movie> {
        val moviesList: MutableList<Movie> = mutableListOf()
        for (movieDTO in moviesListDTO) {
            moviesList.add(
                Movie(
                    id = movieDTO.id,
                    name = movieDTO.title,
                    description = movieDTO.overview,
                    poster = BASE_URL + movieDTO.id + "/image?api_key="+ API_KEY,
                    rating = movieDTO.vote_average,
                    date = movieDTO.release_date
                )
            )
        }
        return moviesList
    }
    override fun findMoviesonDB(
        findStr: String,
        lang: String,
        page: Int,
        includeAdult: Boolean,
        callback: retrofit2.Callback<MoviesListDTO>
    ) {
        remoteDataSource.findMovies(query = findStr, lang = lang, page = page, includeAdult = includeAdult,callback = callback)
    }

}