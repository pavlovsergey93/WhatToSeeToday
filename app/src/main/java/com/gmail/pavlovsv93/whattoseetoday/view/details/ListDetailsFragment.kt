package com.gmail.pavlovsv93.whattoseetoday.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentListDetailsBinding
import com.gmail.pavlovsv93.whattoseetoday.model.AppState
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.WhatToSeeActivity
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.MoviesAdapter
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeViewModel

const val ARG_CATALOG : String = "ARG_CATALOG"

class ListDetailsFragment : Fragment() {

    private lateinit var detailsListModelView : WhatToSeeViewModel
    private var _binding : FragmentListDetailsBinding? = null
    private val binding get() = _binding!!

    companion object{
        fun newInstance(catalog: String) = ListDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CATALOG, catalog)
            }
        }
    }

    private val adapter = MoviesAdapter(object : WhatToSeeActivity.OnClickItem {
        override fun onClick(movie: Movie) {
            val manager = requireActivity().supportFragmentManager
            manager.beginTransaction()
                    .replace(
                            R.id.main_whattosee_container,
                            MovieDetailFragment.newInstance(movie.id)
                    )
                    .addToBackStack("HomeFragment")
                    .commit()
        }

        override fun onClickFavorite(movie: Movie) {
            TODO("Not yet implemented")
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentListDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewListDetails : RecyclerView = binding.detailsListRecyclerview as RecyclerView
        recyclerViewListDetails.layoutManager = GridLayoutManager(requireContext(), 3)

        val catalog = arguments?.getString(ARG_CATALOG)

        detailsListModelView.getData().observe(viewLifecycleOwner, Observer<AppState> { appState ->
                renderData(appState, catalog!!)
        })
            detailsListModelView.getCatalogMoviesRetrofit(catalog!!)

    }

    private fun renderData(appState: AppState?, catalog: String) {
        when(appState){
            is AppState.OnError -> {
                binding.detailsListProgressbar.isVisible = false
                binding.detailsListRecyclerview.isVisible = false
                view?.showSnackBarAction(appState.toString(),getString(R.string.reload),
                        {detailsListModelView.getCatalogMoviesRetrofit(catalog)}
                )
            }
            AppState.OnLoading -> binding.detailsListProgressbar.isVisible = true
            is AppState.OnSuccess -> {
                adapter.setMovie(appState.moviesData)
                binding.detailsListProgressbar.isVisible = false
                binding.detailsListRecyclerview.isVisible = true
            }
        }

    }

    private fun getMoviesCatalog(catalog: String?) {
        //запрос на получение каталога
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}