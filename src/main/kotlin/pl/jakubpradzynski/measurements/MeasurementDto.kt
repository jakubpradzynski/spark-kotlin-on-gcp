package pl.jakubpradzynski.measurements

import java.io.Serializable
import java.sql.Timestamp

data class MeasurementDto(
    val school_name: String,
    val school_street: String?,
    val school_post_code: String,
    val school_city: String,
    val school_longitude: String,
    val school_latitude: String,
    val data_humidity_avg: String,
    val data_pressure_avg: String,
    val data_temperature_avg: String,
    val data_pm10_avg: String,
    val data_pm25_avg: String,
    val timestamp: String
) : Serializable {
    fun toDomainObject(): Measurement = Measurement(
        school = School(
            name = school_name,
            street = school_street,
            postCode = school_post_code,
            city = school_city,
            longitude = school_longitude.toDouble(),
            latitude = school_latitude.toDouble()
        ),
        data = MeasurementData(
            humidityAverage = data_humidity_avg.toDouble(),
            pressureAverage = data_pressure_avg.toDouble(),
            temperatureAverage = data_temperature_avg.toDouble(),
            pm10Average = data_pm10_avg.toDouble(),
            pm25Average = data_pm25_avg.toDouble()
        ),
        timestamp = Timestamp.valueOf(timestamp)
    )
}