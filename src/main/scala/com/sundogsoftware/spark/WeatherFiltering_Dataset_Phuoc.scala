package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.round
import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructType}
object WeatherFiltering_Dataset_Phuoc {
  case class Temperature(stationID: String, date: Int, measure_type: String, temperature: Float)

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder()
      .appName("MinTemperature")
      .master("local[*]")
      .getOrCreate()

    // we define a schema because there is no header in csv file
    val temperatureSchema = new StructType()
      .add("stationID", StringType, nullable = true)
      .add("date", IntegerType, nullable = true)
      .add("measure_type", StringType, nullable = true)
      .add("temperature", FloatType, nullable = true)

    // read the file as dataset
    import spark.implicits._
    val ds = spark.read
      .schema(temperatureSchema)
      .csv("data/1800.csv")
      .as[Temperature]

    // filter out all but TMIN entries
    val minTemps = ds.filter($"measure_type" === "TMIN")

    // select only stationID and temperature
    val stationTemps = minTemps.select("stationID", "temperature")

    // aggregate to find min tem for every station
    // this will create a column name min(temperature)
    val minTempByStation = stationTemps.groupBy("stationID").min("temperature")

    // convert to F
    val minTempByStationF = minTempByStation
      // this will create a new column temperature
      .withColumn("temperature", round($"min(temperature)" * 0.1f * (9.0f / 5.0f) + 32.0f, 2))
      .select("stationID", "temperature").sort("temperature")

    val results = minTempByStationF.collect()

    for (result <- results) {
      val station = result(0)
      val temp = result(1).asInstanceOf[Float]
      val formattedTemp = f"$temp%.2f F"
      println(s"$station minimum temperature: $formattedTemp")

    }
  }
}
