package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.entities.DatabaseAsteroid

@Dao
interface AsteroidDAO {
    @Query("SELECT * FROM DatabaseAsteroid")
    fun getAsteroidList(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}