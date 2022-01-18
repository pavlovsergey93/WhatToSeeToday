package com.gmail.pavlovsv93.whattoseetoday.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentFavoritesBinding
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.AppState
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeFavoritesViewModel
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeHomeViewModel

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: WhatToSeeFavoritesViewModel

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

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

        favoritesViewModel = ViewModelProvider(this).get(WhatToSeeFavoritesViewModel::class.java)
        favoritesViewModel.getData().observe(viewLifecycleOwner, Observer { state ->
            renderData(state)
        })
    }

    private fun renderData(state : AppState) {
        binding.fragmentFavoritesTextview.setText(R.string.message)
        Toast.makeText(requireContext(), "get data", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
