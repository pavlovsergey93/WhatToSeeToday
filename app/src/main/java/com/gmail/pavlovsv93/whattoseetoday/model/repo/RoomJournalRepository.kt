package com.gmail.pavlovsv93.whattoseetoday.model.repo

import android.os.Handler
import android.os.Looper
import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.db.JournalDAO
import com.gmail.pavlovsv93.whattoseetoday.model.db.MoviesEntity
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class RoomJournalRepository(private val journalDS: JournalDAO): RoomInterfaceJournalRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private fun getLocalAll() : List<MoviesEntity> = journalDS.getAllItemDB()

    override fun getLocalAllJournal(callback: Callback<MutableList<Movie>>) {
        executor.execute {
            try {
                val result : MutableList<Movie> = mutableListOf()
                val dbList = getLocalAll()
                for(i in 0 until dbList.size){
                    result.add(Movie(
                            id= dbList[i].idMovie,
                            name = dbList[i].title,
                            description = null,
                            poster = dbList[i].poster,
                            rating = dbList[i].rating,
                            date = dbList[i].date
                    ))
                }
                handler.post{
                    callback.onSuccess(result)
                }
            }catch (exc: Exception){
                callback.onError(exc)
            }
        }
    }

    override fun setItemInDBJournal(movie: Movie) {
        val movieDB: MoviesEntity = MoviesEntity(
                idMovie = movie.id,
                title = movie.name,
                date = movie.date,
                poster = movie.poster,
                rating = movie.rating
        )
        journalDS.setInDB(movieDB)
    }

    override fun delItemToTheDBJournal(idMovie: Int) {
        val result: List<MoviesEntity> = getLocalAll()
        for (i in result.indices) {
            if (result[i].idMovie == idMovie) {
                journalDS.delItemDB(result[i])
                break
            }
        }
    }

    override fun findItem(idMovie: Int): Boolean {
        val result: List<MoviesEntity> = getLocalAll()
        for (i in result.indices) {
            if (result[i].idMovie == idMovie) {
                return true
            }
        }
        return false
    }
}