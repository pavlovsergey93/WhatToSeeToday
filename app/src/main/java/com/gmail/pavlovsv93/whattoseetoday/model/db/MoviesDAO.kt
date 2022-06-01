package com.gmail.pavlovsv93.whattoseetoday.model.db

import androidx.room.*

@Dao
interface MoviesDAO {
    @Insert //Вставить
    fun setInDB(vararg movies: MoviesEntity)

    @Delete // Удалить
    fun delItemDB(movie: MoviesEntity)

    @Query("SELECT * FROM moviesentity") // Получить все элементы дазы данных
    fun getAllItemDB() : List<MoviesEntity>
}