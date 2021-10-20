package com.udacity.asteroidradar.repository

import android.content.Context
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
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class AsteroidRadarRepository(private val database: AsteroidDatabase, private val context: Context) {

    private val _networkErrorMessage: MutableLiveData<String> = MutableLiveData("")
    val networkErrorMessage: LiveData<String> get() = _networkErrorMessage


    private val _loaderStatus: MutableLiveData<Enums.LoaderStatus> = MutableLiveData(Enums.LoaderStatus.DONE)
    val loaderStatus: LiveData<Enums.LoaderStatus> get() = _loaderStatus

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidList()){
        it.asDomaniModel()
    }

    val pictureOfDay: LiveData<PictureOfDay> = Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
        it?.asDomainModel()
    }

    suspend fun refreshAsteroids(endDate: String = "") {
        withContext(Dispatchers.IO){
            try {
                _loaderStatus.postValue(Enums.LoaderStatus.LOADING)
                val result = NetworkService.asteroidService.getAsteroids(endDate = endDate)
                val asteroids: List<AsteroidDTO> = parseAsteroidsJsonResult(JSONObject(result))
                if (asteroids.isNotEmpty()){
                    database.asteroidDao.clearAll()
                }
                database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
                _loaderStatus.postValue(Enums.LoaderStatus.DONE)
                setNetworkErrorMessage("")
            } catch (ex: Exception){
                _loaderStatus.postValue(Enums.LoaderStatus.DONE)
                Timber.d("error", ex)
                setNetworkErrorMessage(context.getString(R.string.network_error))
            }
        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO){
            try {
                _loaderStatus.postValue(Enums.LoaderStatus.LOADING)
                val pictureDayDto = NetworkService.pictureOfDayService.getPictureOfDay()
                database.pictureOfDayDao.insert(pictureDayDto.asDatabaseModel())
                _loaderStatus.postValue(Enums.LoaderStatus.DONE)
                setNetworkErrorMessage("")
            } catch (ex: Exception){
                Timber.d("error " + ex.message)
                _loaderStatus.postValue(Enums.LoaderStatus.DONE)
                setNetworkErrorMessage(context.getString(R.string.network_error))
            }
        }
    }

    fun setNetworkErrorMessage(value: String){
        _networkErrorMessage.postValue(value)
    }
}