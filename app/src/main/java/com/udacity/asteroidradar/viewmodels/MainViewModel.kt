package com.udacity.asteroidradar.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import com.udacity.asteroidradar.utils.Enums
import com.udacity.asteroidradar.utils.Utils
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabaseInstance(application)
    private val repository = AsteroidRadarRepository(database, application.applicationContext)

    val asteroids =  repository.asteroids
    val pictureOfDay =  repository.pictureOfDay
    val loader = repository.loaderStatus
    val networkError = repository.networkErrorMessage

    // detail page navigation handler
    private val _navigateToItemDetail = MutableLiveData<Asteroid?>()
    val navigateToItemDetail: LiveData<Asteroid?>
        get() = _navigateToItemDetail

    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
            repository.refreshPictureOfDay()
        }
    }

    /**
     * When the item is clicked, set the [_navigateToItemDetail] [MutableLiveData]
     * @param item The [Asteroid] that was clicked on.
     */
    fun navigateAsteroidDetails(item: Asteroid){
        _navigateToItemDetail.value = item
    }

    /**
     * After the navigation has taken place, make sure [navigateToItemDetail] is set to null
     */
    fun navigateAsteroidDetailsComplete(){
        _navigateToItemDetail.value = null
    }

    fun filterAsteroidList(filter: Enums.AsteroidFilter){
        viewModelScope.launch {
            when (filter){
                Enums.AsteroidFilter.WEEK -> {
                    val cal: Calendar = Calendar.getInstance()
                    cal.time = Date()
                    cal.add(Calendar.DATE, 7)
                    repository.refreshAsteroids(endDate = Utils.getDateStringFormat(cal.time))
                }
                Enums.AsteroidFilter.TODAY -> repository.refreshAsteroids(endDate = Utils.getToday())
                else -> repository.refreshAsteroids()
            }
            repository.refreshPictureOfDay()
        }
    }

    class MainViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct MainViewModel")
        }
    }
}