package com.gmail.pavlovsv93.whattoseetoday.view.rating

import android.os.Bundle
import android.os.RecoverySystem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.MoviesAdapter
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentRatingBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.details.MovieDetailFragment
import com.gmail.pavlovsv93.whattoseetoday.view.home.HomeFragment
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.AppState
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeViewModel

class RatingFragment : Fragment() {

    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!

    private lateinit var ratingViewModel: WhatToSeeViewModel

    private val adapter = MoviesAdapter(object : HomeFragment.OnClickItem {
        override fun onClick(movie : Movie){
            val manager = requireActivity().supportFragmentManager
            if(manager != null){
                manager.beginTransaction()
                    .replace(R.id.main_whattosee_container, MovieDetailFragment.newInstance(movie.id))
                    .addToBackStack("RatingFragment")
                    .commit()
            }
        }
    })

    companion object {
        fun newInstance() = RatingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.fragmentRatingContainer
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        ratingViewModel = ViewModelProvider(this).get(WhatToSeeViewModel::class.java)
        ratingViewModel.getData().observe(viewLifecycleOwner, Observer { state ->
            renderData(state)
        })
        ratingViewModel.getCatalogMoviesRetrofit("top_rated")
    }

    private fun renderData(state : AppState) {
        when (state) {
            AppState.OnLoading -> binding.fragmentProgbarRating.isVisible = true
            is AppState.OnError -> {
                binding.fragmentRatingTextview.isVisible = true
                binding.fragmentRatingTextview.text = R.string.error.toString()
                view?.showSnackBarAction(state.toString(),getString(R.string.reload), {ratingViewModel.getRatingMovies()})
            }
            is AppState.OnSuccess -> {
                adapter.setMovie(state.moviesData)
                binding.fragmentProgbarRating.isVisible = false
                if (state.moviesData?.size != 0) {
                    binding.fragmentRatingTextview.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}