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
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentHomeBinding
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.AppState
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeHomeViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: WhatToSeeHomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        homeViewModel = ViewModelProvider(this).get(WhatToSeeHomeViewModel::class.java)

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
        homeViewModel.getDataFromDB()

    }

    private fun renderData(state: AppState) {
        when (state) {
            AppState.OnLoading -> binding.fragmentProgbarHome.isVisible = true
            is AppState.OnError -> {
                Snackbar.make(binding.root, state.toString(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Перезагрузить") { homeViewModel.getDataFromDB() }
                    .show()
                Toast.makeText(requireContext(), state.exception.toString(), Toast.LENGTH_LONG)
            }
            is AppState.OnSuccess -> {
                binding.fragmentHomeTextview.setText(R.string.message)
                binding.fragmentProgbarHome.isVisible = false
                val moviesData = state.moviesData
            }
        }

//        binding.fragmentHomeTextview.setText(R.string.message)
//        Toast.makeText(requireContext(), "get data", Toast.LENGTH_SHORT).show();
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
