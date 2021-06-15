package mongoexercise.service

import com.typesafe.scalalogging.Logger
import mongoexercise.dao.JobsDao
import mongoexercise.model.Job
import org.mongodb.scala.Completed
import org.mongodb.scala.result.{DeleteResult, UpdateResult}

import scala.concurrent.Future

class JobService {
  final val logger = Logger(this.getClass.getName)
  final val jobsDao = new JobsDao()

  def addJob(job: Job): Future[Completed] = {
    logger.info("Adding JOB" + job)
    try {
      jobsDao.addJob(job)
    }catch{
      case e : Exception =>
        logger.info(s"Operation Failed $e")
        throw e
    }
  }

  def addJobs(jobs: List[Job]): Future[Completed] = {

    logger.info("Adding JOBs" + jobs.toString())
    try {
      jobsDao.addJobs(jobs)
    }catch{
      case e : Exception =>
        logger.info(s"Operation Failed $e")
        throw e
    }
  }

  def updateJob(jobId:String, job: Job): Future[UpdateResult] = {
    logger.info("Updating JOB" + jobId)
    try {
      jobsDao.updateJob(jobId,job)
    }catch{
      case e : Exception =>
        logger.info(s"Operation Failed $e")
        throw e
    }
  }

  def getJob(jobId:String): Future[Option[Job]] = {
    logger.info("Fetching JOB" + jobId)
    try {
      jobsDao.getJob(jobId)
    }catch{
      case e : Exception =>
        logger.info(s"Operation Failed $e")
        throw e
    }
  }

  def getJob(jobId:String, company:String): Future[Option[Job]] = {
    logger.info(s"Fetching JOB $jobId with $company")
    try {
      jobsDao.getJob(jobId,company)
    }catch{
      case e : Exception =>
        logger.info(s"Operation Failed $e")
        throw e
    }
  }

  def getJobsWithPagination(pageNumber: Int, limit: Int) = {
    val skip : Int = pageNumber*limit
    jobsDao.getJobsWithPagination(skip,limit)
  }

  def getAllJobs(): Future[Seq[Job]] = {
    jobsDao.getAllJobs
  }

  def deleteJob(jobId:String): Future[DeleteResult] = {
    logger.info("Deleting JOB" + jobId)
    try {
      jobsDao.deleteJob(jobId)
    }catch{
      case e : Exception =>
        logger.info(s"Operation Failed $e")
        throw e
    }
  }

}
