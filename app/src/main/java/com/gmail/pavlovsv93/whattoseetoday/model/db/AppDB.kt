package com.gmail.pavlovsv93.whattoseetoday.model.db

import android.app.Application

import androidx.room.Room

class AppDB : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: AppDB? = null
        private var database: MoviesDB? = null
        private const val DB_NAME = "MoviesFavorite"

        fun getMoviesDAO(): MoviesDAO {
            if (database == null) {
                synchronized(MoviesDB::class.java) {
                    if (database == null) {
                        if (instance == null) {
                            throw IllegalAccessException("Application is null")
                        }
                        database = Room.databaseBuilder(instance!!.applicationContext, MoviesDB::class.java, DB_NAME)
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return database!!.moviesDAO()
        }
    }

}