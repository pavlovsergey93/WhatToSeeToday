package com.gmail.pavlovsv93.whattoseetoday.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.ActivityWhatToSeeBinding
import com.gmail.pavlovsv93.whattoseetoday.view.ui.favorites.FavoritesFragment
import com.gmail.pavlovsv93.whattoseetoday.view.ui.rating.RatingFragment
import com.gmail.pavlovsv93.whattoseetoday.view.ui.home.HomeFragment

class WhatToSeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatToSeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_to_see)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_whattosee_container, HomeFragment.newInstance())
            .commit()

        binding = ActivityWhatToSeeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.mainBottomNavView.setOnItemSelectedListener {it ->
            when (it.itemId) {
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