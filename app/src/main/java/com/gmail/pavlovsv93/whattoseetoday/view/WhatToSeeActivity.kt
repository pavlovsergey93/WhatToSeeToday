package com.gmail.pavlovsv93.whattoseetoday.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmail.pavlovsv93.whattoseetoday.BuildConfig
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.ActivityWhatToSeeBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.FavoritesFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.RatingFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.HomeFragment

const val API_KEY = BuildConfig.TMDB_API_KEY
const val BASE_URL = "https://api.themoviedb.org/3/movie/"
const val BASE_URL_RETROFIT = "https://api.themoviedb.org/"
const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"
const val BASE_URL_IMAGE_ORIGIN = "https://image.tmdb.org/t/p/original"

class WhatToSeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatToSeeBinding

    interface OnClickItem {
        fun onClick(movie: Movie)
        fun onClickFavorite(movie: Movie, flag: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhatToSeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_whattosee_container, HomeFragment.newInstance())
                .commit()
        }

        binding = ActivityWhatToSeeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.mainBottomNavView.setOnItemSelectedListener {item ->
            when (item.itemId) {
                 R.id.menu_btnnavview_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_whattosee_container, HomeFragment.newInstance())
                        .commit()
                    true
                }
                R.id.menu_btnnavview_favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_whattosee_container, FavoritesFragment.newInstance())
                        .commit()
                    true
                }
                R.id.menu_btnnavview_rating -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_whattosee_container, RatingFragment.newInstance())
                        .commit()
                    true
                }
                else -> false
            }
        }

    }
}