package com.gmail.pavlovsv93.whattoseetoday.view.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentFavoritesBinding
import com.gmail.pavlovsv93.whattoseetoday.view.ui.WhatToSeeViewModel

class FavoritesFragment : Fragment() {

    private lateinit var viewModel: WhatToSeeViewModel

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

        viewModel = ViewModelProvider(this).get(WhatToSeeViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            renderData()
        })
    }

    private fun renderData() {
        binding.fragmentFavoritesTextview.setText(R.string.message)
        Toast.makeText(requireContext(), "get data", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
