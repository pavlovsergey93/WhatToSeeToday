package com.gmail.pavlovsv93.whattoseetoday.view.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentRatingBinding
import com.gmail.pavlovsv93.whattoseetoday.view.ui.WhatToSeeViewModel

class RatingFragment : Fragment() {

    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WhatToSeeViewModel

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

        viewModel = ViewModelProvider(this).get(WhatToSeeViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            renderData()
        })
    }

    private fun renderData() {
        binding.fragmentRatingTextview.setText(R.string.message)
        Toast.makeText(requireContext(), "get data", Toast.LENGTH_SHORT).show();
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}