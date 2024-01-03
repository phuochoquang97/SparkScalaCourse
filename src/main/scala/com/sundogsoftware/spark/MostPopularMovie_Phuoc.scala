package com.sundogsoftware.spark

import org.apache.log4j._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, LongType, StructType}
import scala.io.{Codec, Source}

object MostPopularMovie_Phuoc {
  case class Movies(userID: Int, movieID: Int, rating: Int, timestamp: Long)

  // load up a map of movie IDs to movie names
  def loadMovieNames() : Map[Int, String] = {
    implicit val codec: Codec = Codec("ISO-8859-1") // the current encoding of u.item, not UTF-8

    var movieNames: Map[Int, String] = Map()

    val lines = Source.fromFile("data/ml-100k/u.item")

    for (line <- lines.getLines()) {
      val fields = line.split('|')
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }
    lines.close()

    movieNames
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder()
      .appName("PopularMovies")
      .master("local[*]")
      .getOrCreate()

    val nameDict = spark.sparkContext.broadcast(loadMovieNames())

    val movieSchema = new StructType()
      .add("userID", IntegerType, nullable = true)
      .add("movieID", IntegerType, nullable = true)
      .add("rating", IntegerType, nullable = true)
      .add("timestamp", LongType, nullable = true)

    // load up movie data set
    import spark.implicits._
    val movies = spark.read
      .option("sep", "\t")
      .schema(movieSchema)
      .csv("data/ml-100k/u.data")
      .as[Movies]

    // get number of reviews per movieID
    val movieCounts = movies.groupBy("movieID").count()

    // we start by declaring an "anonymous function" in Scala
    val lookupName: Int => String = (movieID:Int) => {
      nameDict.value(movieID)
    }

    // wrap it with a udf
    val lookupNameUDF = udf(lookupName)

    val moviesWithNames = movieCounts
      .withColumn("movieTitle", lookupNameUDF(col("movieID")))
    
    val sortedMoviesWithNames = moviesWithNames.sort("count")
    
    sortedMoviesWithNames.show(sortedMoviesWithNames.count.toInt, truncate = false)

    spark.stop()

  }
}
