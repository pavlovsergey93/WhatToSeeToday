package com.gmail.pavlovsv93.whattoseetoday

import android.app.IntentService
import android.content.Intent
import com.gmail.pavlovsv93.whattoseetoday.model.MovieDTO
import com.gmail.pavlovsv93.whattoseetoday.view.API_KEY
import com.gmail.pavlovsv93.whattoseetoday.view.details.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MoviesDetailsService(name: String = "MovieDetails") : IntentService(name) {

    private val broadcastIntent = Intent(DETAIL_INTENT_FILTER)

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val idMovie = intent.getIntExtra(DETAIL_INTENT_ID, 0)
            if (idMovie == 0) {
                onEmptyData()
            } else {
                loadingMovie(id = idMovie)
            }
        }
    }

    private fun onEmptyData() {
        putLoadResult(DETAIL_DATA_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAIL_INTENT_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun loadingMovie(id: Int) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${id}?api_key=${API_KEY}&language=ru-RU")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection
                    .apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                    }
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val movieDTO: MovieDTO = Gson().fromJson(bufferedReader, MovieDTO::class.java)
                onResponse(movieDTO)
            } catch (exc: Exception) {
                onErrorRequest(exc.message ?: "EMPTY ERROR")
            } finally {
                urlConnection.disconnect()
            }
        } catch (exc: MalformedURLException) {
            onMalformedURL()
        }
    }

    private fun onResponse(movieDTO: MovieDTO) {
        if (movieDTO.id == null) {
            onEmptyResponse()
        } else {
            onSuccessReponse(movieDTO.title, movieDTO.overview, movieDTO.vote_average, movieDTO.backdrop_path, movieDTO.id)
        }

    }

    private fun onSuccessReponse(
        title: String,
        overview: String,
        voteAverage: Double,
        backdropPath: String,
        id: Int
    ) {
        putLoadResult(DETAIL_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAIL_TITLE_SUCCESS_EXTRA,title)
        broadcastIntent.putExtra(DETAIL_OVERVIEW_SUCCESS_EXTRA,overview)
        broadcastIntent.putExtra(DETAIL_RATING_SUCCESS_EXTRA,voteAverage)
        broadcastIntent.putExtra(DETAIL_POSTER_SUCCESS_EXTRA,backdropPath)
        broadcastIntent.putExtra(DETAIL_ID_SUCCESS_EXTRA,id)
        sendBroadcast(broadcastIntent)

    }

    private fun onEmptyResponse() {
        putLoadResult(DETAIL_RESPONSE_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(s: String) {
        putLoadResult(DETAIL_ERROR_REQUEST_EXTRA)
        broadcastIntent.putExtra(DETAIL_ERROR_REQUEST_MESSAGE_EXTRA, s)
        sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAIL_URL_MALFORMED_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAIL_LOAD_RESULT_EXTRA, result)

    }
}


