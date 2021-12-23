package com.mobikasa.weather_app.repositories

import com.mobikasa.weather_app.networks.RestAPI

class WeatherRepository(private val restAPI: RestAPI) {

    suspend fun fetchWeatherData(cityName: String) = restAPI.fetchWeather(cityName)
}