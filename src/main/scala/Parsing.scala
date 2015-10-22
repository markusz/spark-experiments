import com.sandinh.phpparser.PhpUnserializer

//import org.json4s.JsonAST.{JValue, JBool, JString, JInt}
//import org.json4s.JsonFormat

import JSON._
import spray.json._

object Parsing {
  def repl(value: String) = {
    val replaced = value.replaceAll(" \\* ", "").replaceAll("PSD\\\\AdExternBundle\\\\Entity\\\\", "")
    replaced
  }

  def cropResultMap(map: Map[String, Any]): Map[String, Any] = {
    val cleanedMap = map.map({
      case (key, value) =>
        val cleanedKey: String = key.replaceAll(" \\* ", "")

        value match {
        case s: Map[String, Any] => cleanedKey -> cropResultMap(value.asInstanceOf[Map[String, Any]])
        case s: Tuple2[String, Map[String, Any]] => cleanedKey -> (
          repl(value.asInstanceOf[Tuple2[String, Map[String, Any]]]._1),
          cropResultMap(value.asInstanceOf[Tuple2[String, Map[String, Any]]]._2))
        case s: Boolean => cleanedKey -> value
        case s: Int => cleanedKey -> value
        case _ => cleanedKey -> value.toString
      }
    })
    cleanedMap
  }

  def parsePHPObjectToJSONString(phpObject: String) = {
    val parsedResultAsMap = PhpUnserializer.parse(phpObject).asInstanceOf[Map[String, Any]]
    val croppedResultAsMap = cropResultMap(parsedResultAsMap)

    croppedResultAsMap.toJson.compactPrint
  }
}
