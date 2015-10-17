import java.io.File
import java.nio.charset.Charset

import org.apache.commons.csv.{CSVFormat, CSVParser}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


object Experiment {
  def main(args: Array[String]) {

    csvExperiment()
    //    textExperiment()
  }

  def csvExperiment() = {
    val sourceFilePath = "./data/R-sample-sets/csv/boot/acme.csv" // Should be some file on your system
    val asFile = new File(sourceFilePath)
    val result = CSVParser.parse(asFile, Charset.defaultCharset(), CSVFormat.newFormat(','))
    val records = result.getRecords

    val config = new SparkConf().setMaster("local").setAppName("Test App")
    val sc = new SparkContext(config)

    val sqlContext = new SQLContext(sc)
    val df = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").load(sourceFilePath)

    //df.select("month", "market", "acme").write.format("com.databricks.spark.csv").save("newcars.csv")

    val data = 1 to 1000

    val distData = sc.parallelize(data)
    val filtered = distData.filter(_ < 10).collect()
    val combinations = filtered.combinations(4)

    val perms: Iterator[Array[Int]] = filtered.permutations
    while(perms.hasNext){
      println(perms.next.deep.mkString(","))
    }

    while(combinations.hasNext){
      println(combinations.next.deep.mkString(","))
    }

    println(combinations.map(r => r.deep.mkString(",")))
    println(filtered.mkString(","))
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