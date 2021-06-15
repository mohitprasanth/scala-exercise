package mongoexercise.util

import mongoexercise.model.Job
import org.json4s.native.JsonMethods

object CommonUtils {
    def fetchJobsFromJson(fileName : String) : List[Job] = {
        implicit val format =  org.json4s.DefaultFormats
        val source = scala.io.Source.fromFile(fileName)
        val jsonString = source.mkString
        val parsed = JsonMethods.parse(jsonString)
        parsed.extract[List[Job]]
    }

}
