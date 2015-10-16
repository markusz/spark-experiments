import java.io.File
import java.net.URL
import java.nio.charset.Charset
import java.util

import org.apache.commons.csv.{CSVRecord, CSVFormat, CSVParser}
import org.apache.spark.{SparkConf, SparkContext}


object Experiments {
  def main(args: Array[String]) {
    csvExperiment()
    //    textExperiment()
  }

  def csvExperiment() = {
    val sourceFilePath = "./data/R-sample-sets/csv/boot/acme.csv" // Should be some file on your system
    val asFile = new File(sourceFilePath)
    val result = CSVParser.parse(asFile, Charset.defaultCharset(), CSVFormat.newFormat(','))
    val records = result.getRecords

    println(records.get(10).get(3))
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
