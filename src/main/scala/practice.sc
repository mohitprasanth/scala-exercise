import mongoexercise.common.DBProperties
import mongoexercise.model.JobGroup
import org.mongodb.scala.MongoCollection

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
val x: MongoCollection[JobGroup] = DBProperties.database.getCollection("jobgroups")
val y = x.find().toFuture()
Await.result(y,5.minutes)