package com.gmail.pavlovsv93.whattoseetoday.view.home

import android.os.Bundle
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
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentHomeBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.showSnackBar
import com.gmail.pavlovsv93.whattoseetoday.view.details.MovieDetailFragment
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.AppState
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeHomeViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    //Ленивая инициализация, первый раз проиницилизируется  на 74 сточке этого класса, и дальше будет использоватся как обычно
    private val homeViewModel: WhatToSeeHomeViewModel by lazy {
        ViewModelProvider(this).get(WhatToSeeHomeViewModel::class.java)
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter = MoviesAdapter(object : OnClickItem {
        override fun onClick(movie: Movie) {
            val manager = requireActivity().supportFragmentManager
            if (manager != null) {
                manager.beginTransaction()
                    .replace(R.id.main_whattosee_container, MovieDetailFragment.newInstance(movie))
                    .addToBackStack("HomeFragment")
                    .commit()
            }
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

        val recyclerView: RecyclerView = binding.fragmentHomeContainer
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.fragmentHomeContainer.adapter = adapter

        val observer =
            Observer<AppState> { state ->                  // Выполняем отслеживание по изменениям liveData. ДЕЙСТВИЕ 1
                renderData(state)
            }
        homeViewModel.getData().observe(
            viewLifecycleOwner,
            observer
        ) // Подписываемся на изменения liveData. ДЕЙСТВИЕ 2

        // ↑ тоже самое в одно действие
        /*viewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            renderData()
        })*/
        homeViewModel.getDataFromDB()

    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.OnLoading -> binding.fragmentProgbarHome.isVisible = true
            is AppState.OnError -> {
                with(binding) {
                    fragmentHomeTextview.isVisible = true
                    fragmentHomeTextview.text = R.string.error.toString()
                }
                view?.showSnackBar(text = state.toString(), actionText = getString(R.string.reload), action = {homeViewModel.getDataFromDB()}  )
//                Snackbar.make(binding.root, state.toString(), Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Перезагрузить") {  }
//                    .show()
                Toast.makeText(requireContext(), state.exception.toString(), Toast.LENGTH_LONG)
            }
            is AppState.OnSuccess -> {
                adapter.setMovie(state.moviesData)
                with(binding) {
                    fragmentProgbarHome.isVisible = false
                    if (state.moviesData?.size != 0) {
                        fragmentHomeTextview.isVisible = false
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface OnClickItem {
        fun onClick(movie: Movie)
    }
}
