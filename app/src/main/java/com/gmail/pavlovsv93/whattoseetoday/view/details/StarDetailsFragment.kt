package com.gmail.pavlovsv93.whattoseetoday.view.details

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
import com.gmail.pavlovsv93.whattoseetoday.databinding.FragmentStarDetailsBinding
import com.gmail.pavlovsv93.whattoseetoday.model.AppStateDetailsStar
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.DetailsStarDTO
import com.gmail.pavlovsv93.whattoseetoday.utils.showSnackBarAction
import com.gmail.pavlovsv93.whattoseetoday.view.BASE_URL_IMAGE_STARS
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.DetailsStarViewModel
import com.squareup.picasso.Picasso

class StarDetailsFragment : Fragment() {

    private val viewModel: DetailsStarViewModel by lazy {
        ViewModelProvider(this).get(DetailsStarViewModel::class.java)
    }
    private var _binding: FragmentStarDetailsBinding? = null
    private val binding get() = _binding!!

    private var uid: Int? = null

    companion object {

        private const val ARG_STAR = "ARG_STAR"

        fun newInstance(uid: Int) =
            StarDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_STAR, uid)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = arguments?.getInt("ARG_STAR") as Int
        viewModel.getData().observe(viewLifecycleOwner, Observer<AppStateDetailsStar> { data ->
            renderData(data)
        })
        viewModel.getDetailsStar(uid!!)
    }

    private fun renderData(data: AppStateDetailsStar?) {
        when(data){
            is AppStateDetailsStar.OnLoading -> {
                binding.detailsStarProgress.isVisible = true
                binding.detailsStarTextview.isVisible = false
            }
            is AppStateDetailsStar.OnError -> {
                binding.detailsStarProgress.isVisible = false
                binding.detailsStarTextview.text = R.string.error.toString()
                view?.showSnackBarAction(data.toString(), getString(R.string.reload), {viewModel.getDetailsStar(uid!!)})
            }
            is AppStateDetailsStar.OnSuccess -> {
                val star: DetailsStarDTO = data.starData
                with(binding){
                    detailsStarProgress.isVisible = false
                    detailsStarTextview.isVisible = false
                    Picasso.with(context)
                        .load(BASE_URL_IMAGE_STARS + star.profile_path)
                        .placeholder(R.drawable.ic_twotone_stars_150)
                        .into(starDetailsImage)
                    starDetailsName.text = star.name
                    starDetailsRating.text = (R.string.rating_text.toString() + star.popularity.toString())
                    starDetailsLive.text = star.biography
                    starDetailsAddress.text = (star.place_of_birth + "\n" + R.string.show_address_to_map)
                    starDetailsAddress.setOnClickListener {
                        Toast.makeText(context, star.place_of_birth, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}