package com.example.pogodynka.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pogodynka.R
import com.example.pogodynka.data.dtos.WeatherDTO
import com.example.pogodynka.data.service.WeatherDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.floor

class SimplerFragment : Fragment() {
    private lateinit var service: WeatherDao
    private lateinit var weather: WeatherDTO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View {
        service = WeatherDao.create()

        return inflater.inflate(R.layout.simpler_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeRequest("Gliwice", view)
        view.findViewById<Button>(R.id.searchButton).apply {
            setOnClickListener {
                val townName = view.findViewById<EditText>(R.id.townNameInput).text.toString()
                makeRequest(townName, view)
            }
        }

        view.findViewById<Switch>(R.id.changeViewSwitch).apply {
            setOnClickListener {
                view.findNavController().navigate(R.id.action_simplerFragment_to_mainFragment)
            }
        }
    }

    private fun makeRequest(townName: String, view: View) {
        service.getWeather(townName).enqueue( object : Callback<WeatherDTO> {
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if (response.body() != null){
                    weather = response.body()!!
                    refreshValue(townName, view)
                }
            }
            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                throw Error("Błąd połączenia z API: ${t.message}")
            }
        })
    }

    private fun refreshValue(townName: String, view: View) {
        view.findViewById<TextView>(R.id.sunriseValue).text = SimpleDateFormat("HH:mm").format(weather.sys.sunrise * 1000)
        view.findViewById<TextView>(R.id.sunsetValue).text = SimpleDateFormat("HH:mm").format(weather.sys.sunset * 1000)
        view.findViewById<TextView>(R.id.windSpeedValue).text = weather.wind.speed.toString() + " km/h"
        view.findViewById<TextView>(R.id.pressureValue).text = weather.main.pressure.toString() + " hPa"
        view.findViewById<TextView>(R.id.temperatureValue).text = ((floor(weather.main.temp - 273.15F)).toInt()).toString()
        view.findViewById<TextView>(R.id.weatherDescription).text = weather.weather[0].main
        view.findViewById<TextView>(R.id.townName).text = townName
        view.findViewById<TextView>(R.id.dateValue).text = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm"))
        view.findViewById<EditText>(R.id.townNameInput).setText("")
        setIcon(view);
    }
    private fun setIcon( view: View){
        val weatherType = weather.weather[0].main;
        if (weatherType == "Clear")
            view.findViewById<ImageView>(R.id.wheatherIcon).setImageResource(R.drawable.sunny);
        if (weatherType == "Clouds")
            view.findViewById<ImageView>(R.id.wheatherIcon).setImageResource(R.drawable.cloudy);
        if (weatherType == "Rain")
            view.findViewById<ImageView>(R.id.wheatherIcon).setImageResource(R.drawable.raining);
        if (weatherType == "Snow")
            view.findViewById<ImageView>(R.id.wheatherIcon).setImageResource(R.drawable.snowing);
        if (weatherType == "Thunderstorm")
            view.findViewById<ImageView>(R.id.wheatherIcon).setImageResource(R.drawable.thunderstorm);
        if (weatherType == "Mist")
            view.findViewById<ImageView>(R.id.wheatherIcon).setImageResource(R.drawable.foggy);

    }
}