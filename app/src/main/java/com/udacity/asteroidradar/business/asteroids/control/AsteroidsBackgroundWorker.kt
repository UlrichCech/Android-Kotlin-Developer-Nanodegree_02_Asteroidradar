package com.udacity.asteroidradar.business.asteroids.control

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.business.persistence.getDatabase
import retrofit2.HttpException

class AsteroidsBackgroundWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)
        return try {
            repository.fetchAsteroids()
            repository.removePastAsteroidEntries()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORKER_NAME = "AsteroidDataBackgroundFetcher"
    }

}