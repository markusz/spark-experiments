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
    //    MachineLearning.ALS()
    //    SQL.testSQL2()

    val samplePHPSerializedObject = "a:2:{i:0;O:50:\"PSD\\AdExternBundle\\Entity\\AdExternComponentDefault\":3:{s:21:\" * agofCodeComponents\";a:1:{i:0;O:43:\"PSD\\AdExternBundle\\Entity\\AdExternComponent\":4:{s:8:\" * title\";s:7:\"channel\";s:7:\" * code\";s:7:\"service\";s:7:\" * type\";O:47:\"PSD\\AdExternBundle\\Entity\\AdExternComponentType\":6:{s:8:\" * title\";s:7:\"channel\";s:13:\" * identifier\";s:7:\"channel\";s:11:\" * position\";i:1;s:10:\" * options\";i:11;s:16:\" * componentType\";i:2;s:13:\" * paramNames\";a:1:{i:0;s:7:\"channel\";}}s:13:\" * identifier\";s:7:\"service\";}}s:9:\" * forced\";b:1;s:8:\" * local\";b:0;}i:1;O:50:\"PSD\\AdExternBundle\\Entity\\AdExternComponentDefault\":3:{s:21:\" * agofCodeComponents\";a:1:{i:0;O:43:\"PSD\\AdExternBundle\\Entity\\AdExternComponent\":4:{s:8:\" * title\";s:3:\"7tv\";s:7:\" * code\";s:3:\"7tv\";s:7:\" * type\";O:47:\"PSD\\AdExternBundle\\Entity\\AdExternComponentType\":6:{s:8:\" * title\";s:16:\"marketingCluster\";s:13:\" * identifier\";s:16:\"marketingcluster\";s:11:\" * position\";i:27;s:10:\" * options\";i:11;s:16:\" * componentType\";i:5;s:13:\" * paramNames\";a:2:{i:0;s:16:\"marketingcluster\";i:1;s:16:\"marketingCluster\";}}s:13:\" * identifier\";i:79;}}s:9:\" * forced\";b:1;s:8:\" * local\";b:0;}}"
    val asJSON = Parsing.parsePHPObjectToJSONString(samplePHPSerializedObject)
    println(asJSON)
  }
}