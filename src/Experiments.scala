
import org.apache.spark.{SparkConf, SparkContext}

object Experiments {
  def main(args: Array[String]) {
    val logFile = "data/2008.csv" // Should be some file on your system

    val config = new SparkConf().setMaster("local").setAppName("Test App")
    val context = new SparkContext(config)

    val logData = context.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("2")).count()
    println("Lines with 2: %s".format(numAs))
    println("Lines total: %s".format(logData.count()))
  }
}
