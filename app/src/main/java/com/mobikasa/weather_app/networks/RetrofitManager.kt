package com.mobikasa.weather_app.networks

import com.mobikasa.weather_app.models.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private fun retrofit(): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL).client(okHttpClient).build()
    }

    val apiService: RestAPI by lazy {
        retrofit().create(RestAPI::class.java)
    }
}