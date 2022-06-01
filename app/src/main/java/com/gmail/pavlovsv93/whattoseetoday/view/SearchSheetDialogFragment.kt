package com.gmail.pavlovsv93.whattoseetoday.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentSearchSheetDialogBinding
import com.gmail.pavlovsv93.whattoseetoday.model.AppState
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.utils.pullCheckSetting
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.details.MovieDetailFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.HomeFragment
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.MoviesAdapter
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.FieldPosition
import java.util.*

class SearchSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var searchViewModel : WhatToSeeViewModel

    private var _binding: FragmentSearchSheetDialogBinding? = null
    private val binding get() = _binding!!

    private val adapter: MoviesAdapter = MoviesAdapter(object : WhatToSeeActivity.OnClickItem {
        override fun onClick(movie: Movie) {
            if (searchViewModel.findItemInJournal(idMovie = movie.id)) {
                searchViewModel.delMovieOnJournal(idMovie = movie.id)
            }
            searchViewModel.setMovieInJournal(movie = movie)

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
            if (!searchViewModel.findItemInMovieDB(movie.id)) {
                searchViewModel.setMovieInFavorite(movie)
            } else {
                searchViewModel.delMovieOnFavorite(idMovie = movie.id)
            }
            showUpdateItem(movie)
        }
    } )

    companion object {
        private const val ARG_FIND = "ARG_FIND"

        fun newInstance(query: String): SearchSheetDialogFragment =
            SearchSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FIND, query)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchSheetDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView : RecyclerView = binding.recuclerviewSheetDialog
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        searchViewModel = ViewModelProvider(this).get(WhatToSeeViewModel::class.java)

        val observer = Observer<AppState> { state ->
            renderData(state)
        }

        searchViewModel.getData().observe(viewLifecycleOwner, observer)



        arguments?.getString(ARG_FIND)?.let {
            searchViewModel.findMoviesOnDB(it, activity?.pullCheckSetting(activity)!!)
        }

    }


    private fun renderData(data: AppState) {
        when (data) {
            AppState.OnLoading -> {
                with(binding){
                    progressSheet.isVisible = true
                    textviewSheet.isVisible = false
                    recuclerviewSheetDialog.isVisible = false
                }
            }
            is AppState.OnError -> {
                with(binding) {
                    progressSheet.isVisible = true
                    textviewSheet.text = R.string.error.toString()
                    textviewSheet.isVisible = true
                }
                view?.showSnackBarAction(
                    data.toString(),
                    getString(R.string.reload),
                    { searchViewModel.getPopularMovies()})
            }
            is AppState.OnSuccess -> {
                adapter.setMovie(data.moviesData)
                binding.recuclerviewSheetDialog.isVisible = true
                binding.progressSheet.isVisible = false
                if (data.moviesData.size != 0) {
                    binding.textviewSheet.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun showUpdateItem(movie: Movie){
        adapter.notifyItemChanged(adapter.updateItem(movie))
    }


}
