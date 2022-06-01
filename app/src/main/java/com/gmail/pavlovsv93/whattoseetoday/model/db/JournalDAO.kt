package com.gmail.pavlovsv93.whattoseetoday.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface JournalDAO {
    @Insert //Вставить
    fun setInDB(vararg movies: MoviesEntity)

    @Delete // Удалить
    fun delItemDB(movie: MoviesEntity)

    @Query("SELECT * FROM moviesentity") // Получить все элементы дазы данных
    fun getAllItemDB() : List<MoviesEntity>
}