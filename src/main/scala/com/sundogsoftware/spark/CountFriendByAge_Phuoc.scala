package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext


object CountFriendByAge_Phuoc {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine, named RatingsCounter
    val sc = new SparkContext("local[*]", "CountFriendByAge_Phuoc")

    // Load up each line of the ratings data into an RDD
    val lines = sc.textFile("data/fakefriends-noheader.csv")

    val rdd = lines.map(parseLine)

    val rddMapValues = rdd.mapValues(x => (x, 1))
    val rddReduceByKey = rddMapValues.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
//    rddReduceByKey.foreach(println)

    // compute avgByAge
    val avgByAge = rddReduceByKey.mapValues(x => (x._1:Float) / x._2)
//    avgByAge.foreach(println)
    // collect and display the results
    val results = avgByAge.collect()
    results.sorted.foreach(println)
  }

  def parseLine(line: String): (Int, Int) = {
    val fields = line.split(",")
    val age = fields(2).toInt
    val numberOfFriends = fields(3).toInt
    (age, numberOfFriends)
  }
}