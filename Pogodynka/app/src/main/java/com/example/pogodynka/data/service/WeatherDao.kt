package com.example.pogodynka.data.service

import com.example.pogodynka.data.dtos.WeatherDTO
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherDao {
    @GET("weather?,pl&appid=f49e94aa7fa1e5d5542dc3a0a9644481")
    fun getWeather(@Query("q") cityName: String) : Call<WeatherDTO>

    companion object {
        var BASE_URL = "http://api.openweathermap.org/data/2.5/"
        fun create() : WeatherDao {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(WeatherDao::class.java)
        }
    }
}