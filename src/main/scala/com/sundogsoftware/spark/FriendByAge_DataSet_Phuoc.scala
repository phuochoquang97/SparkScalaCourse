package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
object FriendByAge_DataSet_Phuoc {
  case class Person(id: Int, name:String, age:Int, friends:Int)
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder()
      .appName("SparkSQL")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._
    val people = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/fakefriends.csv")
      .as[Person]

    people.printSchema()
    println("Group by age:")
    people.groupBy("age").count().show()

    // using SQL
    people.createOrReplaceTempView("people")
    val friendsByAge = spark.sql("SELECT age, COUNT(friends) FROM people GROUP BY age")
    val results = friendsByAge.collect()
    results.foreach(println)

    spark.stop()
  }
}
