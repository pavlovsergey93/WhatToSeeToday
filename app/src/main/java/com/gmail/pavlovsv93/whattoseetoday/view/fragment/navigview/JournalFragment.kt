package com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview

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
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentJournalBinding
import com.gmail.pavlovsv93.whattoseetoday.model.AppState
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.details.MovieDetailFragment
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.JournalAdapter
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.WhatToSeeViewModel

class JournalFragment : Fragment() {

    private val journalViewModel: WhatToSeeViewModel by lazy {
        ViewModelProvider(this).get(WhatToSeeViewModel::class.java)
    }

    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = JournalFragment()
    }

    interface OnClickJournal {
        fun onClickCardJournal(movie: Movie)
    }

    private val adapter = JournalAdapter(object : JournalFragment.OnClickJournal {
        override fun onClickCardJournal(movie: Movie) {
            if(journalViewModel.findItemInJournal(idMovie = movie.id)){
                journalViewModel.delMovieOnJournal(idMovie = movie.id)
            }
            journalViewModel.setMovieInJournal(movie = movie)
            val manager = requireActivity().supportFragmentManager
            if (manager != null) {
                manager.beginTransaction()
                        .replace(
                                R.id.main_whattosee_container,
                                MovieDetailFragment.newInstance(movie.id)
                        )
                        .addToBackStack("JournalFragment")
                        .commit()
            }
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerJournal: RecyclerView = binding.recyclerviewJournal
        recyclerJournal.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerJournal.adapter = adapter

        journalViewModel.getData().observe(viewLifecycleOwner, Observer<AppState> { data ->
            renderData(data)
        })

        journalViewModel.getJournal()
    }

    private fun renderData(data: AppState) {
        when (data) {
            AppState.OnLoading -> {
                with(binding){
                    journalProgressbar.isVisible = true
                    journalTextview.isVisible = false
                    recyclerviewJournal.isVisible = false
                }
            }
            is AppState.OnError -> {
                with(binding) {
                    journalProgressbar.isVisible = true
                    journalTextview.text = R.string.error.toString()
                    journalTextview.isVisible = true
                }
                    view?.showSnackBarAction(
                            data.toString(),
                            getString(R.string.reload),
                            { journalViewModel.getJournal()})
                }
            is AppState.OnSuccess -> {
                adapter.setJournal(data.moviesData)
                binding.recyclerviewJournal.isVisible = true
                binding.journalProgressbar.isVisible = false
                if (data.moviesData.size != 0) {
                    binding.journalProgressbar.isVisible = false
                }
            }
        }
    }
}