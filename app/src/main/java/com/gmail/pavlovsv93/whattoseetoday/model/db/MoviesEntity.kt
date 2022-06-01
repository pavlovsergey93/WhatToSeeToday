package com.gmail.pavlovsv93.whattoseetoday.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoviesEntity(
        @PrimaryKey(autoGenerate = true)
        val uid: Int = 0,
        @ColumnInfo(name = "ID_MOVIE")
        val idMovie: Int,
        @ColumnInfo(name = "TITLE_MOVIE")
        val title: String?,
        @ColumnInfo(name = "RELEASE_MOVIE")
        val date: String?,
        @ColumnInfo(name = "POSTER_MOVIE")
        val poster: String?,
        @ColumnInfo(name = "RATING_MOVIE")
        val rating: Double?

)
