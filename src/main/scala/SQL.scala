import java.sql.{PreparedStatement, DriverManager, Connection}

import org.apache.spark.sql.{DataFrameReader, SQLContext}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.sql.hive._
import spray.json.JsValue


object SQL {
  val URL = "jdbc:mysql://localhost:3306"
  val url = "jdbc:mysql://:3306"
  val username = "root"
  val password = "root"

  def testSQL2() = {
    val query: String = "SELECT * FROM psd_adextern_brands;"


    val config = new SparkConf().setMaster("local[3]").setAppName("Test App")
    val sc = new SparkContext(config)
    val sqlContext = new SQLContext(sc)
    val hiveContext = new HiveContext(sc)

    val mySQLUrl: String = url + "/?user=" + username + "&password=" + password
    var schema = "ivw_prd"
    var tablename = "mineus_ivw_path"
    val jdbcDF = hiveContext.read.format("jdbc").options(
      Map("url" -> mySQLUrl,
        "dbtable" -> (schema + "." + tablename)
      )).load()

    jdbcDF.groupBy("name").count().show()
    //    jdbcDF.cache()
    //    val names = jdbcDF.select("name")
    //    println(jdbcDF.count())
  }

  def getResultsFromMappingTable(lowerBound: Int = 0, upperBound: Int = 1000) = {
    val schemaName = "adextern"
    val tableName = "piranha_mappings_with_offset"

    val sparkConfig = new SparkConf().setMaster("local[2]").setAppName("Mapping Aggregator")
    val sparkContext = new SparkContext(sparkConfig)

    val sqlContext = new SQLContext(sparkContext)
    val hiveContext = new HiveContext(sparkContext)

    val mySQLUrl: String = url + "/?user=" + username + "&password=" + password

    val options = Map(
      "url" -> mySQLUrl,
      "dbtable" -> s"$schemaName.$tableName"
    )

    val hiveDataFrameReader: DataFrameReader = hiveContext.read.format("jdbc").options(options)
    val hiveDataFrame = hiveDataFrameReader.load()

    hiveDataFrame.show()
    println(hiveDataFrame.count())

    hiveDataFrame
    //    hiveDataFrame.show()
  }

  def test() {
    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.jdbc.Driver"

    // there's probably a better way to do this
    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
      statement.setFetchSize(Integer.MIN_VALUE)
      val resultSet = statement.executeQuery("SELECT * FROM adextern.piranha_mappings LIMIT 0,200000")
      var i = 0
      var e = 0
      while (resultSet.next()) {
        i += 1
        val nodeConfigRaw: String = resultSet.getString("nodeConfig")
        val parentConfigRaw: String = resultSet.getString("parentConfig")


        //We deal with references that can not be parsed properly by setting their type to "i"
        def canonicalizeSerializedObject(s: String) = s.replaceAll("\u0000", " ").replaceAll("r:", "i:")

        val nodeConfig = nodeConfigRaw match {
          case s: String => Parsing.parseSerializedPHPObjectToJSONString(canonicalizeSerializedObject(s)) match {
            case null => e += 1
            case ss: JsValue => ss
          }
          case _ => null
        }

        val parentConfig = parentConfigRaw match {
          case s: String => Parsing.parseSerializedPHPObjectToJSONString(canonicalizeSerializedObject(s)) match {
            case null => e += 1
            case ss: JsValue => ss
          }
          case _ => null
        }

        val query = "update users set num_points = ? where first_name = ?";
        val preparedStmt = connection.prepareStatement(query);
        preparedStmt.setInt   (1, 6000);
        preparedStmt.setString(2, "Fred");

        // execute the java preparedstatement
        preparedStmt.executeUpdate();

        println(s"nodeConfigRaw = $nodeConfigRaw")
        println(s"parentConfigRaw = $parentConfigRaw")
        println(s"parsedNodeConfig = $nodeConfig")
        println(s"parsedParentConfig = $parentConfig")
        println(s"----- Total: $i, Error: $e (${(e.toDouble / i.toDouble)}) -----")
      }
    } catch {
      case e: Throwable => e.printStackTrace()
    }
    connection.close()
  }
}
