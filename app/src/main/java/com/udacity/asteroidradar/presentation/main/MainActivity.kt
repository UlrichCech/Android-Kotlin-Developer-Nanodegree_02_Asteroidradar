package com.udacity.asteroidradar.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.business.configuration.GlobalAppConfiguration


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // read the NASA-API-Key from launch-flags: -e "NASA_API_KEY" "<CUSTOM-API-KEY>"

        //
        val key = if (intent?.extras != null) {
            intent?.extras?.getString("NASA_API_KEY", "")
        } else {
            ""
        }
        GlobalAppConfiguration.apiKeyNasa = key!!
    }

}
