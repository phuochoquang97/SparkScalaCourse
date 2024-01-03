package com.sundogsoftware.spark

import org.apache.log4j._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object MostPopularSuperHero_Phuoc {
  case class SuperHeroNames(id: Int, name: String)

  case class SuperHero(value: String)

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder()
      .appName("MostPopularSuperHero")
      .master("local[*]")
      .getOrCreate()

    // create schema when reading Marvel-names.txt
    val superHeroNamesSchema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("name", StringType, nullable = true)

    // build up a hero ID -> name Dataset
    import spark.implicits._
    val names = spark.read
      .schema(superHeroNamesSchema) // non-header
      .option("sep", " ")
      .csv("data/Marvel-names.txt")
      .as[SuperHeroNames]

    // lines contain connection among heroes
    val lines = spark.read
      .text("data/Marvel-graph.txt")

    // process connection
    // the first element in each line is the hero's ID, following are connections
    // a hero can be in multiple lines
    val connections = lines
      .withColumn("id", split(col("value"), " ")(0))
      .withColumn("connections", size(split(col("value"), " ")) - 1)
      .groupBy("id").agg(sum("connections").alias("connections"))

    // get the hero that has the most connections
    val mostPopular = connections
      .sort($"connections".desc)
      .first()

    val mostPopularName = names
      .filter($"id" === mostPopular(0))
      .select("name")
      .first()

    println(s"${mostPopularName(0)} is the most popular superhero with ${mostPopular(1)} co-appearances.")

    // Exercise: find the most obscure superhero (with 1 connection)
    val heroIDWithOneConnection = connections
      .filter($"connections" === 1) // (id, connections)

    // then join with names data set
    val heroNameWithOneConnection = names
      .join(heroIDWithOneConnection, "id")
    println("Heroes with 1 connection:")
    heroNameWithOneConnection.show()

    spark.close()
  }
}
