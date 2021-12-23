package com.mobikasa.weather_app.models

sealed class Resources<out T> {
    data class Loading<T>(val data: T? = null) : Resources<T>()
    data class Success<T>(val data: T? = null) : Resources<T>()
    data class Error(val error: String? = null, val responseCode: Int? = null) :
        Resources<Nothing>()
}