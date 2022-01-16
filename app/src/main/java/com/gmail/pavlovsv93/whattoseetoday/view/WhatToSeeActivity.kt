package com.gmail.pavlovsv93.whattoseetoday.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.view.ui.main.HomeFragment

class WhatToSeeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_to_see)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_whattosee_container, HomeFragment.newInstance())
            .commit()

    }
}