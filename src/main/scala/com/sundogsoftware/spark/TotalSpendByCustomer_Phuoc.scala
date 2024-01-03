package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object TotalSpendByCustomer_Phuoc {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local", "TotalSpendByCustomer_Phuoc")

    val lines = sc.textFile("data/customer-orders.csv")

    val orders = lines.map(parseLine)
    val totalSpend = orders.map(x => (x._1, x._3)).reduceByKey((x, y) => x + y)
    val result = totalSpend.collect()

    for (r <- result.map(x => (x._2, x._1)).sorted) {
      val customerId = r._2
      val spend = r._1
      println(s"$customerId spends $spend USD")
    }

  }

  def parseLine(line:String): (Int, Int, Float) = {
    val order = line.split(",")
    val customerId = order(0).toInt
    val productId = order(1).toInt
    val price = order(2).toFloat
    (customerId, productId, price)
  }
}
