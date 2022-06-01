package com.gmail.pavlovsv93.whattoseetoday.model.repo

import android.os.Handler
import android.os.Looper
import com.gmail.pavlovsv93.whattoseetoday.model.Callback
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.db.MoviesDAO
import com.gmail.pavlovsv93.whattoseetoday.model.db.MoviesEntity
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class RoomRepository(private val localDS: MoviesDAO) : RoomInterfaceRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun getLocalAll(callback: Callback<MutableList<Movie>>) {
        executor.execute {
            try {
                val result : MutableList<Movie> = mutableListOf()
                val dbList = getAllLocalMovies()
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



    override fun setItemInDB(movie: Movie) {
        val movieDB: MoviesEntity = MoviesEntity(
                idMovie = movie.id,
                title = movie.name,
                date = movie.date,
                poster = movie.poster,
                rating = movie.rating
        )
        localDS.setInDB(movieDB)
    }

    override fun delItemToTheDB(idMovie: Int) {
        val result: List<MoviesEntity> = getAllLocalMovies()
        for (i in result.indices) {
            if (result[i].idMovie == idMovie) {
                localDS.delItemDB(result[i])
                break
            }
        }
    }
    override fun getAllLocalMovies(): List<MoviesEntity> = localDS.getAllItemDB()

    override fun findItem(idMovie: Int): Boolean {
        val result: List<MoviesEntity> = getAllLocalMovies()
        for (element in result) {
            if (element.idMovie == idMovie) {
                return true
            }
        }
        return false
    }
}