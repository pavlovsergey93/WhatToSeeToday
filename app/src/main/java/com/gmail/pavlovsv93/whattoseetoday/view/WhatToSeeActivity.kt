package com.gmail.pavlovsv93.whattoseetoday.view

import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentResultListener
import com.gmail.pavlovsv93.whattoseetoday.BuildConfig
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.ActivityWhatToSeeBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.BasSuggestionProvider
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.FavoritesFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.RatingFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.HomeFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.ARG_CONTACT_NUMBER
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.ContactsFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.JournalFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.SettingFragment

const val API_KEY = BuildConfig.TMDB_API_KEY
const val BASE_URL = "https://api.themoviedb.org/3/movie/"
const val BASE_URL_RETROFIT = "https://api.themoviedb.org/"
const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"
const val BASE_URL_IMAGE_ORIGIN = "https://image.tmdb.org/t/p/original"
const val TAG_SHEET = "SearchSheetDialogFragment"

class WhatToSeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatToSeeBinding

    interface OnClickItem {
        fun onClick(movie: Movie)
        fun onClickFavorite(movie: Movie)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWhatToSeeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_whattosee_container, HomeFragment.newInstance())
                .commit()
        }

        val toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)

        handleIntent(intent)

        binding.mainBottomNavView.setOnItemSelectedListener { item ->
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

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.mainDrawerLayout,
            binding.mainToolbar,
            R.string.appbar_scrolling_view_behavior,
            R.string.bottom_sheet_behavior
        )
        binding.mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.mainNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_navview_setting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_whattosee_container, SettingFragment.newInstance())
                        .addToBackStack("Setting")
                        .commit()
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menu_navview_journal -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_whattosee_container, JournalFragment.newInstance())
                        .addToBackStack("Journal")
                        .commit()
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menu_navview_contacts -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_whattosee_container, ContactsFragment.newInstance())
                        .addToBackStack("Контакты")
                        .commit()
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
        supportFragmentManager.setFragmentResultListener(ARG_CONTACT_NUMBER, this, FragmentResultListener { requestKey, result ->

            val number: String? = result.getString(requestKey)
            // Отправить запрос в другое приложение
        })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchInfo = searchManager.getSearchableInfo(componentName)
        val searchView = menu?.findItem(R.id.search_bar)?.actionView as SearchView
        searchView.setSearchableInfo(searchInfo)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                SearchRecentSuggestions(
                    this@WhatToSeeActivity,
                    BasSuggestionProvider.AUTHORITY,
                    BasSuggestionProvider.MODE
                ).saveRecentQuery(query, null)
                SearchSheetDialogFragment.newInstance(query.toString())
                    .show(supportFragmentManager, TAG_SHEET)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // поиск мгновенный
                Log.d(TAG, "onQueryTextChange: $newText")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun handleIntent(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent?.action) {
            intent.getStringExtra(SearchManager.QUERY)
        }
    }

}
