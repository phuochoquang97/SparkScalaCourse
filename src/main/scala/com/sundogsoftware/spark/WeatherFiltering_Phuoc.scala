package com.sundogsoftware.spark

import breeze.linalg.min
import org.apache.log4j._
import org.apache.spark._

object WeatherFiltering_Phuoc {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "WeatherFiltering_Phuoc")
    val lines = sc.textFile("data/1800.csv")
    val parsedLines = lines.map(parseLine)
    val minTemps = parsedLines.filter(x => x._2 == "TMIN")

    // we no longer need entryType
    val stationTemps = minTemps.map(x => (x._1, x._3.toFloat))
    val minTempsByStation = stationTemps.reduceByKey((x, y) => min(x, y))
    val results = minTempsByStation.collect()
    for (result <- results.sorted) {
      val station = result._1
      val temp = result._2
      val formattedTemp = f"$temp%.2f F"
      println(s"$station minimum temperature: $formattedTemp")
    }
  }

  def parseLine(line: String): (String, String, Float) = {
    val fields = line.split(",")
    val stationId = fields(0)
    val entryType = fields(2)
    val temperature = fields(3).toFloat * 0.1f * 1.8f + 32.0f
    // output is (stationId, entryType, temperature)
    (stationId, entryType, temperature)
  }


}

