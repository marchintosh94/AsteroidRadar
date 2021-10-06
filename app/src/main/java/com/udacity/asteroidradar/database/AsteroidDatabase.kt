package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.database.dao.AsteroidDAO
import com.udacity.asteroidradar.database.dao.PictureOfDayDAO
import com.udacity.asteroidradar.database.entities.DatabaseAsteroid
import com.udacity.asteroidradar.database.entities.DatabasePictureOfDay

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfDay::class], version = 1)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDAO
    abstract val pictureOfDayDao: PictureOfDayDAO
}

private lateinit var DB_INSTANCE: AsteroidDatabase

fun getDatabaseInstance(context: Context): AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java){
        if (!::DB_INSTANCE.isInitialized){
            DB_INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroidDB"
            ).build()
        }
    }
    return DB_INSTANCE
}