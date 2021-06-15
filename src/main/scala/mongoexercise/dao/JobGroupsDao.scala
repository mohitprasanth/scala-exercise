package mongoexercise.dao

import mongoexercise.common.{Constants, DBProperties}
import mongoexercise.dao.JobGroupsDao.jobGroups
import mongoexercise.model.JobGroup
import org.mongodb.scala.model.Filters.{equal, in}
import org.mongodb.scala.{MongoCollection, MongoDatabase}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object JobGroupsDao{
  val db: MongoDatabase = DBProperties.database
  implicit  val jobGroups: MongoCollection[JobGroup] = db.getCollection(Constants.JOB_GROUPS_COLLECTIONS)
}

class JobGroupsDao {
    def getAllJobGroups(): Future[Seq[JobGroup]] = {
      jobGroups.find().toFuture()
    }

  def getJobGroup(jobGroupId : String): Future[Option[JobGroup]] = {
    jobGroups.find(equal("groupId",jobGroupId)).headOption()
  }

  def getJobGroups(jobGroupIds : List[String]): Future[Seq[JobGroup]] = {
    val g = jobGroups.find(in("groupId",jobGroupIds:_*)).toFuture()
    val r = Await.result(g,3.minute)
    g
  }
}
