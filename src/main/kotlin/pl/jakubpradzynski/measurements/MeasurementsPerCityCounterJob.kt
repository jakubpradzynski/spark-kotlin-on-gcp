package pl.jakubpradzynski.measurements

import org.apache.spark.sql.DataFrameReader
import org.jetbrains.kotlinx.spark.api.`as`
import org.jetbrains.kotlinx.spark.api.groupByKey
import org.jetbrains.kotlinx.spark.api.map
import org.jetbrains.kotlinx.spark.api.withSpark

fun main() {
    withSpark(appName = "Measurements per city counter") {
        val inputPath = "gs://example-data/esa_air_pollution_measurements_poland.csv"
        val resultPath = "gs://example-data/measurements_per_city.csv"
        println("Starting job...")
        spark
            .read()
            .setDelimiter(";")
            .firstRowAsHeader()
            .csv(inputPath)
            .`as`<MeasurementDto>()
            .map { it.toDomainObject() }
            .groupByKey { it.school.city }
            .count()
            .repartition(1)
            .write()
            .csv(resultPath)
        println("Job done.")
    }
}

private fun DataFrameReader.setDelimiter(delimiter: String) = this.option("delimiter", delimiter)
private fun DataFrameReader.firstRowAsHeader() = this.option("header", "true")
