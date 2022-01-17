package com.gmail.pavlovsv93.whattoseetoday.view.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentHomeBinding
import com.gmail.pavlovsv93.whattoseetoday.view.ui.WhatToSeeViewModel

class HomeFragment : Fragment() {

    private lateinit var viewModel: WhatToSeeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get()= _binding!!

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

        viewModel = ViewModelProvider(this).get(WhatToSeeViewModel::class.java)

        // Выполняем отслеживание по изменениям liveData. ДЕЙСТВИЕ 1
        val observer = Observer<Any> { data ->
            renderData()
        }

        // Подписываемся на изменения liveData. ДЕЙСТВИЕ 2
        viewModel.getData().observe(viewLifecycleOwner, observer)

        // ↑ тоже самое в одно действие
        /*viewModel.getData().observe(viewLifecycleOwner, Observer { data ->
            renderData()
        })*/

    }

    private fun renderData() {
        binding.fragmentHomeTextview.setText(R.string.message)
        Toast.makeText(requireContext(), "get data", Toast.LENGTH_SHORT).show();
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}