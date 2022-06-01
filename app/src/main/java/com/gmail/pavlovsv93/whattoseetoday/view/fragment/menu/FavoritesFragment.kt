package com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.MoviesAdapter
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentFavoritesBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.details.MovieDetailFragment
import com.gmail.pavlovsv93.whattoseetoday.model.AppState
import com.gmail.pavlovsv93.whattoseetoday.view.WhatToSeeActivity
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeViewModel

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: WhatToSeeViewModel

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val adapter = MoviesAdapter(object : WhatToSeeActivity.OnClickItem {
        override fun onClick(movie: Movie){
            if(favoritesViewModel.findItemInJournal(idMovie = movie.id)){
                favoritesViewModel.delMovieOnJournal(idMovie = movie.id)
            }
            favoritesViewModel.setMovieInJournal(movie = movie)
            val manager = requireActivity().supportFragmentManager
            if(manager != null){
                manager.beginTransaction()
                    .replace(R.id.main_whattosee_container, MovieDetailFragment.newInstance(movie.id))
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        }

        override fun onClickFavorite(movie: Movie) {
            if (!favoritesViewModel.findItemInMovieDB(movie.id)) {
                favoritesViewModel.setMovieInFavorite(movie)
            } else {
                favoritesViewModel.delMovieOnFavorite(idMovie = movie.id)
            }
            showUpdateItem(movie)
        }
    })

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = binding.fragmentFavoritesContainer
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        favoritesViewModel = ViewModelProvider(this).get(WhatToSeeViewModel::class.java)
        favoritesViewModel.getData().observe(viewLifecycleOwner, Observer { state ->
            renderData(state)
        })
        favoritesViewModel.getMoviesFavorite()
    }

    private fun renderData(state : AppState) {
        when (state) {
            AppState.OnLoading -> binding.fragmentProgbarFavorites.isVisible = true
            is AppState.OnError -> {
                binding.fragmentFavoritesTextview.isVisible = true
                binding.fragmentFavoritesTextview.text = R.string.error.toString()
                view?.showSnackBarAction(state.toString(),getString(R.string.reload), {favoritesViewModel.getNewMovies()})
            }
            is AppState.OnSuccess -> {
                adapter.setMovie(state.moviesData)
                binding.fragmentProgbarFavorites.isVisible = false
                if (state.moviesData?.size != 0) {
                    binding.fragmentFavoritesTextview.isVisible = false
                }
            }
        }
    }

    fun showUpdateItem(movie: Movie){
        adapter.notifyItemChanged(adapter.updateItem(movie))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
