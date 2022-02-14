package com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview

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
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentStarsBinding
import com.gmail.pavlovsv93.whattoseetoday.model.AppStateStars
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.ResultDTO
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.details.StarDetailsFragment
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.StarsAdapter
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.StarsViewModel

class StarsFragment : Fragment() {
    interface OnClickToStar{
        fun onClickToStar(star: ResultDTO)
    }

    private val viewModel: StarsViewModel by lazy{
        ViewModelProvider(this).get(StarsViewModel::class.java)
    }

    private var _binding : FragmentStarsBinding? = null
    private val binding get() = _binding!!

    private val adapter : StarsAdapter = StarsAdapter(object : OnClickToStar{

        override fun onClickToStar(star: ResultDTO) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_whattosee_container, StarDetailsFragment.newInstance(star.id))
                .addToBackStack(star.name)
                .commit()
        }

    })

    companion object{
        fun newInstance() = StarsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.recyclerviewStars
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        viewModel.getData().observe(viewLifecycleOwner, Observer<AppStateStars> { data ->
            renderData(data)
        })
        viewModel.getPopularStarsOnDB()
    }

    private fun renderData(data: AppStateStars) {
        when (data) {
            AppStateStars.OnLoading -> {
                with(binding){
                    fragmentStarsProgress.isVisible = true
                    fragmentStarsTextview.isVisible = false
                    recyclerviewStars.isVisible = false
                }
            }
            is AppStateStars.OnError -> {
                with(binding) {
                    fragmentStarsProgress.isVisible = true
                    fragmentStarsTextview.text = R.string.error.toString()
                    fragmentStarsTextview.isVisible = true
                }
                view?.showSnackBarAction(
                    data.toString(),
                    getString(R.string.reload),
                    { viewModel.getPopularStarsOnDB()})
            }
            is AppStateStars.OnSuccess -> {
                adapter.setStarsList(data.starsData)
                binding.recyclerviewStars.isVisible = true
                binding.fragmentStarsProgress.isVisible = false
                if (data.starsData.size != 0) {
                    binding.fragmentStarsTextview.isVisible = false
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}