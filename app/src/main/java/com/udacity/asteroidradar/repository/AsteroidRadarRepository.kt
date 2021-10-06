package com.udacity.asteroidradar.repository

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidDTO
import com.udacity.asteroidradar.api.NetworkService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.entities.asDomainModel
import com.udacity.asteroidradar.database.entities.asDomaniModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AsteroidRadarRepository(private val database: AsteroidDatabase) {

    private val _networkErrorMessage: MutableLiveData<String> = MutableLiveData("")
    val networkErrorMessage: LiveData<String> get() = _networkErrorMessage


    private val _loaderStatus: MutableLiveData<Enums.LoaderStatus> = MutableLiveData(Enums.LoaderStatus.DONE)
    val loaderStatus: LiveData<Enums.LoaderStatus> get() = _loaderStatus

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidList()){
        it.asDomaniModel()
    }

    val pictureOfDay: LiveData<PictureOfDay> = Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO){
            Enums.LoaderStatus.LOADING
            NetworkService.asteroidService.getAsteroids().enqueue(object : Callback<JSONObject> {
                override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                    Enums.LoaderStatus.DONE
                    response.body()?.let {
                        val asteroids: List<AsteroidDTO> = parseAsteroidsJsonResult(it)
                        database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
                    }
                }

                override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                    Enums.LoaderStatus.DONE
                    _networkErrorMessage.value = Resources.getSystem().getString(R.string.network_error)
                }
            })
        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO){
            try {
                Enums.LoaderStatus.LOADING
                val pictureDayDto = NetworkService.pictureOfDayService.getPictureOfDay().await()
                database.pictureOfDayDao.insert(pictureDayDto.asDatabaseModel())
                Enums.LoaderStatus.DONE
            } catch (ex: Exception){
                Enums.LoaderStatus.DONE
                _networkErrorMessage.value = Resources.getSystem().getString(R.string.network_error)
            }

        }
    }
}