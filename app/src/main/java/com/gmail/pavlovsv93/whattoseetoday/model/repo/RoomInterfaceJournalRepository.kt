package com.gmail.pavlovsv93.whattoseetoday.model.repo

import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Movie

interface RoomInterfaceJournalRepository {

    fun getLocalAllJournal(callback: Callback<MutableList<Movie>>)
    fun setItemInDBJournal(movie : Movie)
    fun delItemToTheDBJournal(idMovie : Int)
    fun findItem(idMovie: Int): Boolean
}