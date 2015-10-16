name := "SparkExperiment"

version := "1.0"

scalaVersion := "2.11.7"

// additional libraries
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.1" % "compile",
  "com.databricks" %% "spark-csv" % "1.2.0" % "compile"
)