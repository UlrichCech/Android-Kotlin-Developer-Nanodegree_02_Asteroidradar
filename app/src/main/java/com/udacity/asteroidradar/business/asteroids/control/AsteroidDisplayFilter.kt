package com.udacity.asteroidradar.business.asteroids.control

import com.udacity.asteroidradar.business.configuration.GlobalAppConfiguration
import java.text.SimpleDateFormat
import java.util.*

abstract class AsteroidDisplayFilter {

    private val dateFormat = SimpleDateFormat(GlobalAppConfiguration.API_QUERY_DATE_FORMAT, Locale.getDefault())

    fun getStartDate(): String {
        val calendar = Calendar.getInstance()
        return getFormattedDate(calendar.time)
    }

    abstract fun getEndDate(): String

    protected fun getFormattedDate(date: Date): String {
        return dateFormat.format(date)
    }
}



object ShowAllAsteroidFilter: AsteroidDisplayFilter() {
    override fun getEndDate(): String {return "N/A"}
}

object ShowTodayAsteroidFilter: AsteroidDisplayFilter() {
    override fun getEndDate(): String {
        return getStartDate()
    }
}

object ShowWeekAsteroidFilter: AsteroidDisplayFilter() {
    override fun getEndDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, GlobalAppConfiguration.DEFAULT_END_DATE_DAYS)
        return getFormattedDate(calendar.time)
    }
}
