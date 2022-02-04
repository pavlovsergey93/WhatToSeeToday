package com.gmail.pavlovsv93.whattoseetoday.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MoviesEntity::class], version = 1)
abstract class JournalDB : RoomDatabase() {
    abstract fun journalDAO(): JournalDAO
}