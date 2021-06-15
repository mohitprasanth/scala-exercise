package mongoexercise.dao

import mongoexercise.common.{Constants, DBProperties}
import mongoexercise.dao.JobsDao.jobsCollection
import mongoexercise.model.Job
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, MongoCollection, MongoDatabase}

import scala.concurrent.Future

object JobsDao {
  val db: MongoDatabase = DBProperties.database
  private val jobsCollection: MongoCollection[Job] = db.getCollection[Job](Constants.JOBS_COLLECTION)
}

class JobsDao{
  def getJobsWithPagination(noOfRecordsToSkip: Int, limitPerPage: Int) = {
    val jobIDAsc : Bson = ascending("jobId");
    jobsCollection.find().sort(jobIDAsc).skip(noOfRecordsToSkip).limit(limitPerPage).toFuture()
  }


  def addJob(job: Job): Future[Completed] = {
    jobsCollection.insertOne(job).toFuture()
  }

  def addJobs(jobs: List[Job]): Future[Completed] = {
    jobsCollection.insertMany(jobs).toFuture()
  }

  def updateJob(id:String, job: Job): Future[UpdateResult] = {
    jobsCollection.replaceOne(equal("jobId", id), job).toFuture()
  }

  def getJob(jobId: String, company: String): Future[Option[Job]] = {
    jobsCollection.find(and(equal("jobId", jobId), equal("company", company))).headOption()
  }

  def getJob(jobId: String): Future[Option[Job]] = {
    jobsCollection.find(equal("jobId", jobId)).headOption()
  }

  def deleteJob(jobId: String): Future[DeleteResult] ={
    jobsCollection.deleteOne(equal("jobId",jobId)).toFuture()
  }

  def getAllJobs = {
    jobsCollection.find().toFuture()
  }

}