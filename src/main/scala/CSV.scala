import java.io.File
import java.nio.charset.Charset

import org.apache.commons.csv.{CSVFormat, CSVParser}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}

object CSV {
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
    distData.cache()

    val sample: RDD[Int] = distData.sample(true, 0.02)
    val filtered = distData.filter(_ < 10).collect()
    val combinations = filtered.combinations(4)

    val perms: Iterator[Array[Int]] = filtered.permutations
    /*
        while (perms.hasNext) {
          println(perms.next.deep.mkString(","))
        }
    */

    /*
        while (combinations.hasNext) {
          //println(combinations.next.deep.mkString(","))
        }
    */

    println(filtered.mkString(","))
    println(sample.collect().mkString(","))
  }

}
