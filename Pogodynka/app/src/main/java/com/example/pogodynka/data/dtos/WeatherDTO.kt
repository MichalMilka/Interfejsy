package com.example.pogodynka.data.dtos

data class WeatherDTO(
    var weather: List<DescriptionDTO>,
    var main: TemperatureDTO,
    var wind: WindDTO,
    var sys: SunPhaseDTO,
    var name: String
)