import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.hive._


object SQL {
  val URL = "jdbc:mysql://localhost:3306/adextern"

  def testSQL2() = {
    val query: String = "SELECT * FROM psd_adextern_brands;"

    val url = "jdbc:mysql://:3306"
    val username = "root"
    val password = ""

    val config = new SparkConf().setMaster("local").setAppName("Test App")
    val sc = new SparkContext(config)
    val sqlContext = new SQLContext(sc)
    val hiveContext = new HiveContext(sc)

    val mySQLUrl: String = url + "/?user=" + username + "&password=" + password
    var schema = "adextern"
    var tablename = "psd_adextern_brands"
    val jdbcDF = hiveContext.read.format("jdbc").options(
      Map("url" -> mySQLUrl,
        "dbtable" -> (schema + "." + tablename)
      )).load()

    jdbcDF.cache()
    val first_names = jdbcDF.select("name")
    first_names.show(false)
    println(jdbcDF.count())
  }
}
