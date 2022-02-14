package com.gmail.pavlovsv93.whattoseetoday.view.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gmail.pavlovsv93.whattoseetoday.viewmodel.StarsViewModel

class StarDetailsFragment : Fragment() {

    private val viewModel: StarsViewModel by lazy {
        ViewModelProvider(this).get(StarsViewModel::class.java)
    }

    companion object {

        private const val ARG_STAR = "ARG_STAR"

        fun newInstance(uid: Int) =
            StarDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_STAR, uid)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("ARG_STAR")?.let {
            viewModel.getDetailsStar(it)
        }
    }
}