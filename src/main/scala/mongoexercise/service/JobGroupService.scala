package mongoexercise.service

import com.typesafe.scalalogging.Logger
import mongoexercise.dao.{JobGroupsDao, JobsDao}
import mongoexercise.model.{Job, JobGroup}

import scala.concurrent.Future

class JobGroupService {
  final val logger = Logger(this.getClass.getName)
  final val jobGroupsDao = new JobGroupsDao()
  final val jobsDao = new JobsDao()
  implicit val context = scala.concurrent.ExecutionContext.global

  def getAllJobGroups: Future[Seq[JobGroup]] = {
    logger.info("Fetching all jobGroups")
    try{
      jobGroupsDao.getAllJobGroups()
    }catch{
      case ex : Exception =>
        logger.error("Error while fetching job groups")
        throw ex
    }
  }


  private def checkIfJobBelongsToGroup(jobGroup: Option[JobGroup], job: Job): Boolean = {
    if(jobGroup.isDefined){
      for(rule <- jobGroup.get.rules.getOrElse(List())){
        if(!rule.checkIfApplicable(job)){
           return false
        }
      }
      return true
    }
    false
  }

  def getAllJobs(jobGroupId : String): Future[Seq[Job]] = {
    logger.info(s"Fetching all jobs corresponding to jobGroup : $jobGroupId")
    try{
      val jobGroupFuture = jobGroupsDao.getJobGroup(jobGroupId)
      val jobsFuture = jobsDao.getAllJobs
      jobGroupFuture.flatMap(jobGroup => jobsFuture.map(jobList => jobList.filter( job => checkIfJobBelongsToGroup(jobGroup,job))))
    }catch{
      case ex : Exception =>
        logger.error("Error while fetching job groups")
        throw ex
    }
  }

  def getJobGroup(jobGroupId : String): Future[Option[JobGroup]] = {
    jobGroupsDao.getJobGroup(jobGroupId)
  }

  def getAllJobsForJobGroupList(jobGroupList : List[String]) = {
    Future.foldLeft(jobGroupList.map(jgId => getAllJobs(jgId)))(List.empty[Job])(_ ++ _)
  }
}
