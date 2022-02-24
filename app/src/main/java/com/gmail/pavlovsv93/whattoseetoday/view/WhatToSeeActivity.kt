package com.gmail.pavlovsv93.whattoseetoday.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentResultListener
import com.gmail.pavlovsv93.whattoseetoday.BuildConfig
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.databinding.ActivityWhatToSeeBinding
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.BasSuggestionProvider
import com.gmail.pavlovsv93.whattoseetoday.model.Contact
import com.gmail.pavlovsv93.whattoseetoday.utils.getPermission
import com.gmail.pavlovsv93.whattoseetoday.utils.showRationaleDialog
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.FavoritesFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.RatingFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.menu.HomeFragment
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.installations.FirebaseInstallations
import java.io.IOException

const val API_KEY = BuildConfig.TMDB_API_KEY
const val BASE_URL = "https://api.themoviedb.org/3/movie/"
const val BASE_URL_RETROFIT = "https://api.themoviedb.org/"
const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"
const val BASE_URL_IMAGE_ORIGIN = "https://image.tmdb.org/t/p/original"
const val BASE_URL_IMAGE_STARS = "https://image.tmdb.org/t/p/original"
const val TAG_SHEET = "SearchSheetDialogFragment"
const val REFRESH_PERIOD = 60000L
const val MINIMAL_DISTANCE = 100f

class WhatToSeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatToSeeBinding

    private lateinit var mapflag: BottomNavigationView

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

        mapflag = binding.mainBottomNavView
        mapflag.setOnItemSelectedListener { item ->
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
                        .addToBackStack("Contacts")
                        .commit()
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menu_navview_stars -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_whattosee_container, StarsFragment.newInstance())
                        .addToBackStack("Popular Stars")
                        .commit()
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
        supportFragmentManager.setFragmentResultListener(
            KEY_CONTACT_NUMBER,
            this,
            FragmentResultListener { requestKey, result ->
                val contact = result.getParcelable<Contact>(ARG_CONTACT_NUMBER)
                val number = contact?.number
                if (number != null) {
                    val intentR = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intentR);
                    }
                }
                // или так
                //intent.setData(Uri.parse("tel:$number"));

            })
        getToken()
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

        menu?.findItem(R.id.geo).setOnMenuItemClickListener {
            when (this.getPermission(this)) {
                1 -> {
                    getGeoLocation()
                }
                -1 -> {
                    Toast.makeText(this, "Нет доступа к GPS", Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnMenuItemClickListener false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        createPermissionResult(requestCode, grantResults)
    }

    private fun createPermissionResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if (grantResults.size == grantedPermissions) {
                        getGeoLocation()
                    } else {
                        showDialogs(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialogs(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialogs(title: String, message: String) {
        this.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }

    }

    @SuppressLint("MissingPermission")
    private fun getGeoLocation() {
        this?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Получить менеджер геолокаций
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        // Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialogs(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialogs(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                this.showRationaleDialog(this)
            }
        }

    }

    private val onLocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            applicationContext?.let { context ->
                getAddressAsync(context, location)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun showAddressDialog(address: String, location: Location) {
        this?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.dialog_address_title)
                .setMessage(address)
                .setNegativeButton(R.string.dialog_button_close) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


    private fun handleIntent(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent?.action) {
            intent.getStringExtra(SearchManager.QUERY)
        }
    }

    private fun getAddressAsync(
        context: Context,
        location: Location
    ) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                mapflag.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun getToken(){
        FirebaseInstallations.getInstance().id
            .addOnCompleteListener(OnCompleteListener{task ->
                if (!task.isSuccessful){
                    // не удалось получить токен
                    return@OnCompleteListener
                }

                //Полуен токен
                val token = task.result!!

                // Сохранить токен ...
            })
    }

}
