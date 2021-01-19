package com.udacity.asteroidradar.business.asteroids.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.collections.ArrayList

@Entity
data class AsteroidEntity constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)


fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map { asteroidEntity ->
        Asteroid (
            id = asteroidEntity.id,
            codename = asteroidEntity.codename,
            closeApproachDate = asteroidEntity.closeApproachDate,
            absoluteMagnitude = asteroidEntity.absoluteMagnitude,
            estimatedDiameter = asteroidEntity.estimatedDiameter,
            relativeVelocity= asteroidEntity.relativeVelocity,
            distanceFromEarth = asteroidEntity.distanceFromEarth,
            isPotentiallyHazardous = asteroidEntity.isPotentiallyHazardous)
    }
}

fun asDatabaseModel(asteroids: ArrayList<Asteroid>): Array<AsteroidEntity> {
    return asteroids.map { asteroid ->
        AsteroidEntity(
            id = asteroid.id,
            codename = asteroid.codename,
            closeApproachDate = asteroid.closeApproachDate,
            absoluteMagnitude = asteroid.absoluteMagnitude,
            estimatedDiameter = asteroid.estimatedDiameter,
            relativeVelocity= asteroid.relativeVelocity,
            distanceFromEarth = asteroid.distanceFromEarth,
            isPotentiallyHazardous = asteroid.isPotentiallyHazardous)
    }.toTypedArray()
}
