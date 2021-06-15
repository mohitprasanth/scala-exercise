package mongoexercise.model

import org.joda.time.DateTime


case class JobGroup(groupId: String, rules: Option[List[Rule]], priority : Option[Int],createdDate : Option[DateTime])
