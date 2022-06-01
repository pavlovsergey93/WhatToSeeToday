package com.gmail.pavlovsv93.whattoseetoday.model.repo

import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.db.MoviesEntity

interface RoomInterfaceRepository {

    fun getLocalAll(callback: Callback<MutableList<Movie>>)
    fun setItemInDB(movie : Movie)
    fun delItemToTheDB(idMovie : Int)
    fun getAllLocalMovies(): List<MoviesEntity>
    fun findItem(idMovie: Int): Boolean

}