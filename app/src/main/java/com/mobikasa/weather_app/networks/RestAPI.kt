package com.mobikasa.weather_app.networks

import com.mobikasa.weather_app.models.Constants
import com.mobikasa.weather_app.models.ServiceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestAPI {

    @GET("weather")
    suspend fun fetchWeather(
        @Query("q") query: String,
        @Query("appid") apiKey: String = Constants.API_KEY,
    ): Response<ServiceResponse>
}