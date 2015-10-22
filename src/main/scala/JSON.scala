import spray.json._

object JSON extends DefaultJsonProtocol {

  implicit object MapJsonFormat extends JsonFormat[Map[String, Any]] {
/*
    // 1
    def write(m: Map[String, Any]) = {
      JsObject(m.map {
        case (k, v) => JsObject(k -> JsString("as"))
        case _ => JsObject("as" -> JsString("as"))
      })
    }
*/

    def write(m: Map[String, Any]) = {
      JsObject(m.mapValues {
        case v: String => JsString(v)
        case v: Int => JsNumber(v)
        case v: Boolean => JsBoolean(v)
        case v: Map[String, Any] => write(v)
        case v: Tuple2[String, Map[String, Any]] => JsArray(JsString(v._1), write(v._2))
        case v: Any => JsString(v.toString)
      })
    }

    def read(value: JsValue) = ??? // 6
  }
}