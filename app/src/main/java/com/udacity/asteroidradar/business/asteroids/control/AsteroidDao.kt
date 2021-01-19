package com.udacity.asteroidradar.business.asteroids.control

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.business.asteroids.entity.AsteroidEntity

@Dao
interface AsteroidDao {

    @Query("select * from AsteroidEntity ae order by ae.closeApproachDate")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("select * from AsteroidEntity ae where ae.closeApproachDate = :todayAsString")
    fun getTodayAsteroids(todayAsString: String): LiveData<List<AsteroidEntity>>

    @Query("select * from AsteroidEntity ae where ae.closeApproachDate >= :startDate and ae.closeApproachDate <= :endDate order by ae.closeApproachDate")
    fun getAsteroidsForPeriod(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("delete from AsteroidEntity where closeApproachDate < :today")
    fun deletePastAsteroidEntries(today: String)

}
