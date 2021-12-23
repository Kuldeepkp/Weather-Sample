package com.mobikasa.weather_app

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobikasa.weather_app.databinding.ActivityMainBinding
import com.mobikasa.weather_app.extension.dismissKeyboard
import com.mobikasa.weather_app.models.Resources
import com.mobikasa.weather_app.models.ServiceResponse
import com.mobikasa.weather_app.networks.RetrofitManager
import com.mobikasa.weather_app.repositories.WeatherRepository
import com.mobikasa.weather_app.viewmodels.VMFactory
import com.mobikasa.weather_app.viewmodels.WeatherViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private val repository = WeatherRepository(RetrofitManager.apiService)
    private val viewModel by viewModels<WeatherViewModel> { VMFactory(repository) }
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        viewModel.liveData.observe(this) {
            when (it) {
                is Resources.Error -> {
                    mainBinding.mProgressBar.visibility = View.GONE
                    mainBinding.dataLayout.visibility = View.GONE
                    mainBinding.errorTextView.visibility = View.VISIBLE
                    mainBinding.errorTextView.text = it.error
                }
                is Resources.Loading -> {
                    mainBinding.dataLayout.visibility = View.GONE
                    mainBinding.errorTextView.visibility = View.GONE
                    mainBinding.mProgressBar.visibility = View.VISIBLE
                }
                is Resources.Success -> {
                    mainBinding.dataLayout.visibility = View.VISIBLE
                    mainBinding.errorTextView.visibility = View.GONE
                    mainBinding.mProgressBar.visibility = View.GONE
                    setDataToUI(it.data)
                }
            }
        }
    }

    private fun setDataToUI(data: ServiceResponse?) {
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${data?.weather?.get(0)?.icon}@2x.png")
            .placeholder(R.drawable.ic_downloading)
            .into(mainBinding.imageWeatherStatus)
        mainBinding.textTemperatureStatus.text =
            String.format(Locale.getDefault(), "%.0f°", data?.main?.temp)
        mainBinding.textFeelsLikeTemp.text =
            String.format(Locale.getDefault(), "%.0f°", data?.main?.feelsLike)
        mainBinding.textDescriptionStatus.text = data?.weather?.first()?.description
        mainBinding.textMainStatus.text = data?.weather?.first()?.main
        mainBinding.textHumidity.text =
            getString(R.string.humidity_data, data?.main?.humidity.toString())
        mainBinding.textWind.text = "${data?.wind?.speed} m/s"
        mainBinding.textPressure.text = "${data?.main?.pressure} hPa"
        mainBinding.textLocationStatus.text = data?.name
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu.findItem(R.id.search_bar)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Enter city name..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.fetchWeatherData(query)
                    dismissKeyboard()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}