package com.udacity.asteroidradar.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import com.udacity.asteroidradar.utils.Utils
import retrofit2.HttpException

class FetchNewDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object{
        const val WORK_NAME = "FetchNewDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabaseInstance(applicationContext)
        val repository = AsteroidRadarRepository(database, applicationContext)
        return try {
            database.asteroidDao.deleteYesterdayAsteroids()
            repository.refreshAsteroids(Utils.getToday())
            repository.refreshPictureOfDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}