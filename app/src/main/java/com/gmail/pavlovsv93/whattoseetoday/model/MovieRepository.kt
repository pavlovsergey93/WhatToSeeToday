package com.gmail.pavlovsv93.whattoseetoday.model

import android.os.Build
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.gmail.pavlovsv93.whattoseetoday.view.API_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files.lines
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MovieRepository : MovieInterfaceRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var urlConnection: HttpsURLConnection

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getPopularMovies(callback: Callback<MutableList<Movie>>) {
        executor.execute {
            val uri = URL("https://api.themoviedb.org/3/movie/popular?api_key=${API_KEY}&language=ru-RU")
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val convertBufferToString: String =
                    bufferedReader.lines().collect(Collectors.joining("\n"))
                val jsonObject = JSONObject(convertBufferToString)
                val typeToken = object : TypeToken<List<MovieDTO>>() {}.type

                val moviesListDTO =
                    Gson().fromJson<List<MovieDTO>>(jsonObject.getString("results"), typeToken)

                val moviesList: MutableList<Movie> = mutableListOf()

                for (movieDTO in moviesListDTO) {
                    moviesList.add(
                        Movie(
                            id = movieDTO.id,
                            name = movieDTO.title,
                            description = movieDTO.overview,
                            poster = movieDTO.backdrop_path,
                            rating = movieDTO.vote_average
                        )
                    )
                }
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

}
