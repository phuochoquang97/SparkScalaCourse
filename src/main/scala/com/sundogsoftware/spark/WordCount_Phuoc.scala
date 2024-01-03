package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object WordCount_Phuoc {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local", "WordCount_Phuoc")

    val input = sc.textFile("data/book.txt")

    val words = input.flatMap(x => x.split("\\W+"))

    val lowercaseWords = words.map(x => x.toLowerCase())

    val wordCounts = lowercaseWords.map(x => (x, 1)).reduceByKey((x, y) => x + y)

    // flip (word, count) into (count, word) and then sort by key
    val wordCountsSorted = wordCounts.map(x => (x._2, x._1)).sortByKey()
    val results = wordCountsSorted.collect()
    for (result <- results) {
      val count = result._1
      val word = result._2
      println(s"$word: $count")

    }
    wordCountsSorted.foreach(println)
  }
}
