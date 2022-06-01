package com.gmail.pavlovsv93.whattoseetoday.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.gmail.pavlovsv93.whattoseetoday.MoviesDetailsService
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentMovieDetailBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie

const val DETAIL_INTENT_FILTER = "DETAIL_INTENT_FILTER"
const val DETAIL_INTENT_ID = "DETAIL_INTENT_ID"
const val DETAIL_RESPONSE_SUCCESS_EXTRA = "DETAIL_RESPONSE_SUCCESS_EXTRA"
const val DETAIL_URL_MALFORMED_EXTRA = "DETAIL_URL_MALFORMED_EXTRA"
const val DETAIL_RESPONSE_EMPTY_EXTRA = "DETAIL_RESPONSE_EMPTY_EXTRA"
const val DETAIL_ERROR_REQUEST_EXTRA = "DETAIL_ERROR_REQUEST_EXTRA"
const val DETAIL_ERROR_REQUEST_MESSAGE_EXTRA = "DETAIL_ERROR_REQUEST_MESSAGE_EXTRA"
const val DETAIL_DATA_EMPTY_EXTRA = "DETAIL_DATA_EMPTY_EXTRA"
const val DETAIL_INTENT_EMPTY_EXTRA = "DETAIL_INTENT_EMPTY_EXTRA"
const val DETAIL_LOAD_RESULT_EXTRA = "DETAIL_LOAD_RESULT_EXTRA"
const val DETAIL_TITLE_SUCCESS_EXTRA = "DETAIL_TITLE_SUCCESS_EXTRA"
const val DETAIL_OVERVIEW_SUCCESS_EXTRA = "DETAIL_OVERVIEW_SUCCESS_EXTRA"
const val DETAIL_POSTER_SUCCESS_EXTRA = "DETAIL_POSTER_SUCCESS_EXTRA"
const val DETAIL_ID_SUCCESS_EXTRA = "DETAIL_ID_SUCCESS_EXTRA"
const val DETAIL_RATING_SUCCESS_EXTRA = "DETAIL_RATING_SUCCESS_EXTRA"

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    companion object {

        const val ARG_MOVIE: String = "ARG_MOVIE"

        fun newInstance(idMovie: Int) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MOVIE, idMovie)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.registerReceiver(loadResultReceiver, IntentFilter(DETAIL_INTENT_FILTER))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(ARG_MOVIE)
            ?.let {
                getMovie(it)
            }
    }

    private fun getMovie(idMovie: Int) {
        binding.detailsProgressBar.isVisible = true
        context
            ?.let {
                it.startService(Intent(it, MoviesDetailsService::class.java)
                    .apply {
                        putExtra(DETAIL_INTENT_ID, idMovie)
                    })
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        context?.unregisterReceiver(loadResultReceiver)
    }

    private val loadResultReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            when (intent?.getStringExtra(DETAIL_LOAD_RESULT_EXTRA)) {
                DETAIL_INTENT_EMPTY_EXTRA -> TODO()
                DETAIL_DATA_EMPTY_EXTRA -> TODO()
                DETAIL_RESPONSE_EMPTY_EXTRA -> TODO()
                DETAIL_ERROR_REQUEST_EXTRA -> TODO()
                DETAIL_URL_MALFORMED_EXTRA -> TODO()
                DETAIL_RESPONSE_SUCCESS_EXTRA -> display(
                    Movie(
                        name = intent.getStringExtra(DETAIL_TITLE_SUCCESS_EXTRA)!!,
                        description = intent.getStringExtra(DETAIL_OVERVIEW_SUCCESS_EXTRA)!!,
                        rating = intent.getDoubleExtra(DETAIL_RATING_SUCCESS_EXTRA,0.0),
                        id = intent.getIntExtra(DETAIL_ID_SUCCESS_EXTRA,0),
                        poster = intent.getStringExtra(DETAIL_POSTER_SUCCESS_EXTRA,)
                    )
                )
                else -> TODO()
            }
        }
    }

    private fun display(movie: Movie) {
        with(binding) {
            if (movie.poster != null) {

            }
            detailsTitle.text = movie.name
            detailsDescription.text = movie.description
            detailsRating.rating = movie.rating.toFloat()
        }
    }

}

