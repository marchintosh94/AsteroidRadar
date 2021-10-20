package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.entities.DatabaseAsteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

@Dao
interface AsteroidDAO {
    @Query("SELECT * FROM DatabaseAsteroid WHERE DATE(closeApproachDate) >= DATE(:date) ORDER BY closeApproachDate ASC")
    fun getAsteroidList(date: String = Utils.getToday()): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM DatabaseAsteroid")
    fun clearAll()

    @Query("DELETE FROM DatabaseAsteroid WHERE DATE(closeApproachDate) < DATE()")
    fun deleteYesterdayAsteroids()
}