package com.gmail.pavlovsv93.whattoseetoday.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.ActivityWhatToSeeBinding.bind
import com.gmail.pavlovsv93.whattoseetoday.databinding.ActivityWhatToSeeBinding.inflate
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentMovieDetailBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    companion object {

        const val ARG_MOVIE: String = "ARG_MOVIE"

        // подход Kotlin
        fun newInstance(movie: Movie) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_MOVIE, movie)
            }
        }
        // подход Java
//        fun newInstance(movie: Movie): MovieDetailFragment {
//            val mdf = MovieDetailFragment()
//            val data = Bundle()
//            data.putParcelable(ARG_MOVIE, movie)
//            mdf.arguments = data
//            return mdf
//        }
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
        arguments?.getParcelable<Movie>(ARG_MOVIE)?.let { movie ->
            with(binding) {
                if (movie.poster != null) {
                    detailsImage.setImageDrawable(movie.poster)
                }
                detailsTitle.text = movie.name
                detailsDescription.text = movie.description
            }
        }}

        //val movie = requireArguments().getParcelable(ARG_MOVIE) as? Movie

//        with(binding) {
//            if (movie != null) {
//                if (movie.poster != null) {
//                    detailsImage.setImageDrawable(movie.poster)
//                }
//                detailsTitle.text = movie.name
//                detailsDescription.text = movie.description
//            }
//
//        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

