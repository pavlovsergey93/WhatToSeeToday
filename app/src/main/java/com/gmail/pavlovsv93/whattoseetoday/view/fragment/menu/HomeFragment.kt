package com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.MoviesAdapter
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentHomeBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.details.MovieDetailFragment
import com.gmail.pavlovsv93.whattoseetoday.model.AppState
import com.gmail.pavlovsv93.whattoseetoday.view.WhatToSeeActivity
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: WhatToSeeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var page = 1
    private var isLoading: Boolean = false
    private var totalItemCount = 0
    private var lastVisebleItem = 0


    private val adapter = MoviesAdapter(object : WhatToSeeActivity.OnClickItem {
        override fun onClick(movie: Movie) {
            if (homeViewModel.findItemInJournal(idMovie = movie.id)) {
                homeViewModel.delMovieOnJournal(idMovie = movie.id)
            }
            homeViewModel.setMovieInJournal(movie = movie)

            val manager = requireActivity().supportFragmentManager
            if (manager != null) {
                manager.beginTransaction()
                    .replace(
                        R.id.main_whattosee_container,
                        MovieDetailFragment.newInstance(movie.id)
                    )
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        }

        override fun onClickFavorite(movie: Movie) {
            if (!homeViewModel.findItemInMovieDB(movie.id)) {
                homeViewModel.setMovieInFavorite(movie)
            } else {
                homeViewModel.delMovieOnFavorite(idMovie = movie.id)
            }
            showUpdateItem(movie)
        }
    })

    companion object {

        fun newInstance() = HomeFragment()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView: RecyclerView = binding.fragmentHomeContainerPopular
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        homeViewModel = ViewModelProvider(this).get(
            WhatToSeeViewModel::
            class.java
        )

        // Выполняем отслеживание по изменениям liveData. ДЕЙСТВИЕ 1
        val observer = Observer<AppState> { state ->
            renderData(state)
        }

        // Подписываемся на изменения liveData. ДЕЙСТВИЕ 2
        homeViewModel.getData().observe(viewLifecycleOwner, observer)

        // ↑ тоже самое в одно действие
        /*viewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            renderData()
        })*/

        homeViewModel.getCatalogMoviesRetrofit("now_playing")

    }

    private fun renderData(state: AppState) {
        when (state) {
            AppState.OnLoading -> binding.fragmentProgbarHome.isVisible = true
            is AppState.OnError -> {
                binding.fragmentHomeTextview.isVisible = true
                binding.fragmentHomeTextview.text = R.string.error.toString()
                view?.showSnackBarAction(
                    state.toString(),
                    getString(R.string.reload),
                    { homeViewModel.getNewMovies() })
            }
            is AppState.OnSuccess -> {
                adapter.setMovie(state.moviesData)
                binding.fragmentHomeContainerPopular.isVisible = true
                binding.fragmentProgbarHome.isVisible = false
                if (state.moviesData?.size != 0) {
                    binding.fragmentHomeTextview.isVisible = false
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
