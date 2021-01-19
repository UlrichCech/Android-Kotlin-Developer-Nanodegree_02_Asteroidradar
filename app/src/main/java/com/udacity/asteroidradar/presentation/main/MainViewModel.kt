package com.udacity.asteroidradar.presentation.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.business.asteroids.entity.Asteroid
import com.udacity.asteroidradar.business.asteroids.control.AsteroidDisplayFilter
import com.udacity.asteroidradar.business.asteroids.control.AsteroidsRepository
import com.udacity.asteroidradar.business.asteroids.control.ShowTodayAsteroidFilter
import com.udacity.asteroidradar.business.asteroids.control.ShowWeekAsteroidFilter
import com.udacity.asteroidradar.business.configuration.GlobalAppConfiguration
import com.udacity.asteroidradar.business.persistence.getDatabase
import com.udacity.asteroidradar.business.pictureofday.entity.PictureOfDay
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


enum class PictureOfTheDayApiStatus {LOADING, ERROR, DONE}


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    // Status of getting the PictureOfTheDay
    private val _podStatus = MutableLiveData<PictureOfTheDayApiStatus>()
    val podStatus: LiveData<PictureOfTheDayApiStatus>
        get() = _podStatus

    private val _pod = MutableLiveData<PictureOfDay>()
    val pod: LiveData<PictureOfDay>
        get() = _pod

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails


    val asteroids = Transformations.switchMap(asteroidsRepository.displayFilter) {
        when (it) {
            ShowWeekAsteroidFilter -> asteroidsRepository.weekAsteroids
            ShowTodayAsteroidFilter -> asteroidsRepository.todayAsteroids
            else -> asteroidsRepository.allAsteroids
        }
    }


    init {
        getPictureOfTheDay()
        viewModelScope.launch {
            asteroidsRepository.fetchAsteroids()
        }
    }


    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                _podStatus.value = PictureOfTheDayApiStatus.LOADING
                val podResult =
                    NasaApi.retrofitService.getPictureOfTheDay(GlobalAppConfiguration.apiKeyNasa)
                _podStatus.value = PictureOfTheDayApiStatus.DONE
                // IMPORTANT: only process images (videos are currently not supported)
                if (podResult.mediaType == "image") {
                    _pod.value = podResult
                }
            } catch (e: Exception) {
                _podStatus.value = PictureOfTheDayApiStatus.ERROR
            }
        }
    }

    fun setFilter(filter: AsteroidDisplayFilter) {
        asteroidsRepository.displayFilter.postValue(filter)
    }

    fun displayAsteroidDetails(selectedAsteroid: Asteroid) {
        _navigateToAsteroidDetails.value = selectedAsteroid
    }

    fun doneNavigatingToAsteroidDetails() {
        _navigateToAsteroidDetails.value = null
    }



    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


    /**
     * ViewModel-Factory
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}
