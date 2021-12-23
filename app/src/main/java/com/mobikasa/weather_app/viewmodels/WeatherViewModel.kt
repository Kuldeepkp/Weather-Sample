package com.mobikasa.weather_app.viewmodels

import androidx.lifecycle.*
import com.mobikasa.weather_app.models.Resources
import com.mobikasa.weather_app.models.ServiceResponse
import com.mobikasa.weather_app.repositories.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val mData = MutableLiveData<Resources<ServiceResponse>>()
    val liveData: LiveData<Resources<ServiceResponse>>
        get() = mData

    init {
        fetchWeatherData("Delhi")
    }

     fun fetchWeatherData(cityName: String) {
        viewModelScope.launch {
            mData.postValue(Resources.Loading())
            try {
                val response = repository.fetchWeatherData(cityName)
                if (response.isSuccessful && response.code() == 200) {
                    mData.postValue(Resources.Success(data = response.body()))
                } else {
                    mData.postValue(Resources.Error(error = "Something went wrong ${response.message()}",
                        responseCode = response.code()))
                }
            } catch (mException: Exception) {
                mData.postValue(Resources.Error(error = "Something went wrong ${mException.localizedMessage}"))
            }
        }
    }
}