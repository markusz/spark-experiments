import java.io.File
import java.nio.charset.Charset

import org.apache.commons.csv.{CSVFormat, CSVParser}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.{ALS, Rating, MatrixFactorizationModel}

object Experiment {
  def main(args: Array[String]) {

    Logger.getLogger("org.apache.spark").setLevel(Level.OFF)
    //    CSV.csvExperiment()
    MachineLearning.ALS()

    SQL.testSQL2()

    //    textExperiment()
  }

  def machineLearning() = {

  }


  def textExperiment() = {
    val logFile = "data/R-sample-sets/csv/boot/acme.csv" // Should be some file on your system

    val config = new SparkConf().setMaster("local").setAppName("Test App")
    val context = new SparkContext(config)

    val logData = context.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("2")).count()
    println("Lines with 2: %s".format(numAs))
    println("Lines total: %s".format(logData.count()))
  }

}