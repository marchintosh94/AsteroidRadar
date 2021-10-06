package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.udacity.asteroidradar.database.entities.DatabasePictureOfDay

@Dao
interface PictureOfDayDAO {
    @Query("SELECT * FROM DatabasePictureOfDay LIMIT 1")
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = REPLACE)
    fun insert(picture: DatabasePictureOfDay)
}