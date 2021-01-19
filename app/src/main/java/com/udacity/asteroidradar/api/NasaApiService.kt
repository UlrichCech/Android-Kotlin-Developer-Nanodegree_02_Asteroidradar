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

package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.business.configuration.GlobalAppConfiguration
import com.udacity.asteroidradar.business.pictureofday.entity.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(GlobalAppConfiguration.BASE_URL)
        .build()


interface NasaApiService {

    // eg. https://api.nasa.gov/planetary/apod?api_key=<API-KEY>
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey : String) : PictureOfDay

    // eg. https://api.nasa.gov/neo/rest/v1/feed?start_date=2021-01-17&end_date=2021-01-17&api_key=<API-KEY>
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key") apiKey : String) : String

}


object NasaApi {
    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}
