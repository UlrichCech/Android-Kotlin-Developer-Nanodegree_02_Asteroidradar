/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.business.asteroids.control

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.business.asteroids.entity.Asteroid
import com.udacity.asteroidradar.business.asteroids.entity.asDatabaseModel
import com.udacity.asteroidradar.business.asteroids.entity.asDomainModel
import com.udacity.asteroidradar.business.configuration.GlobalAppConfiguration
import com.udacity.asteroidradar.business.persistence.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * The repository for the asteroids.
 */
class AsteroidsRepository(private val database: AsteroidDatabase) {

    var displayFilter = MutableLiveData<AsteroidDisplayFilter>(ShowAllAsteroidFilter)


    val allAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    /**
     * IMPORTANT: In order to evaluate the parameter, as soon as "filter" changes and to have a correct
     * query parameter for "startdate", you have to use Transformations.switchMap() as a wrapper
     * around the transformation to the domain model
     */
    val todayAsteroids: LiveData<List<Asteroid>> =
        Transformations.switchMap(displayFilter) { filter ->
            Transformations.map(database.asteroidDao.getTodayAsteroids(filter.getStartDate())) {
                it.asDomainModel()
            }
        }

    /**
     * IMPORTANT: In order to evaluate the parameter, as soon as "filter" changes and to have a correct
     * query parameter for "startDate" and "endDate", you have to use Transformations.switchMap() as a wrapper
     * around the transformation to the domain model
     */
    val weekAsteroids: LiveData<List<Asteroid>> =
        Transformations.switchMap(displayFilter) { filter ->
            Transformations.map(database.asteroidDao.getAsteroidsForPeriod(filter.getStartDate(), filter.getEndDate())) {
                it.asDomainModel()
            }
        }

    suspend fun fetchAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val jsonString =
                    NasaApi.retrofitService.getAsteroids(GlobalAppConfiguration.apiKeyNasa)
                val jsonObject = JSONObject(jsonString)
                val asteroidList = parseAsteroidsJsonResult(jsonObject)
                database.asteroidDao.insertAll(*asDatabaseModel(asteroidList))
            } catch (e: Exception) {
                Log.i("UCE", e.stackTraceToString())
//                _status.value = MarsApiStatus.ERROR
            }
        }
    }

    suspend fun removePastAsteroidEntries() {
        withContext(Dispatchers.IO) {
            val dateFormat = SimpleDateFormat(GlobalAppConfiguration.API_QUERY_DATE_FORMAT, Locale.getDefault())
            database.asteroidDao.deletePastAsteroidEntries(dateFormat.format(Calendar.getInstance().time))
        }
    }
}


