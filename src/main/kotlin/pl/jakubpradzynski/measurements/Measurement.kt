package pl.jakubpradzynski.measurements

import java.io.Serializable
import java.sql.Timestamp

data class Measurement(
    val school: School,
    val data: MeasurementData,
    val timestamp: Timestamp
) : Serializable

data class School(
    val name: String,
    val street: String?,
    val postCode: String,
    val city: String,
    val longitude: Double,
    val latitude: Double
) : Serializable

data class MeasurementData(
    val humidityAverage: Double,
    val pressureAverage: Double,
    val temperatureAverage: Double,
    val pm10Average: Double,
    val pm25Average: Double
) : Serializable
