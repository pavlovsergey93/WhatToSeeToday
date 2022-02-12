package com.gmail.pavlovsv93.whattoseetoday

import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gmail.pavlovsv93.whattoseetoday.R

class test : Fragment() {
    private var rv: RecyclerView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv = view.findViewById(R.id.fragment_rating_container)

    }
}