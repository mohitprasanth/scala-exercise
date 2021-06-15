package mongoexercise.service

import java.io.{File, PrintWriter}

import com.typesafe.scalalogging.Logger
import mongoexercise.dao.PublisherDao
import mongoexercise.model.Publisher
import mongoexercise.util.JsonHelper

import scala.concurrent.Future
import scala.util.{Failure, Success}

class PublisherService extends JsonHelper {
  final val logger = Logger(this.getClass.getName)
  implicit val context = scala.concurrent.ExecutionContext.global
  final val publisherDao = new PublisherDao()
  final val clientService = new ClientService()
  final val jobGroupService  = new JobGroupService()

  def getAllPublisher() = {
    logger.info("Fetching all Publishers")
    try{
      publisherDao.getAllPublishers()
    }catch{
      case ex : Exception =>
        logger.error("Error while fetching publisher details")
        throw ex
    }
  }

  def getPublisher(publisherId : String): Future[Option[Publisher]] = {
    logger.info(s"Fetching publisher : $publisherId")
    try{
      publisherDao.getPublisher(publisherId)
    }catch{
      case ex : Exception =>
        logger.error("Error while fetching publisher details")
        throw ex
    }
  }

  def generatePublisherFeed(publisherId: String, jobGroupIdList: List[String])= {
    print(publisherId + "   " + jobGroupIdList.toString())
    val jobs = jobGroupService.getAllJobsForJobGroupList(jobGroupIdList)
    jobs.onComplete({
      case Success(value) =>
        val file_Object = new File(s"D:\\kalahast\\IdeaProjects\\ScalaExercise\\src\\main\\resources\\$publisherId.json" )
        val print_Writer = new PrintWriter(file_Object)
        val jsonString = write(value)
        print_Writer.write(jsonString)
        print_Writer.close()
      case Failure(exception) =>
        logger.error("Error while fetching publisher details")
        throw exception
    })
  }

  def generateFeed(publisherId : String) = {
    publisherDao.getPublisher(publisherId)
      .map(p => p.get.clientId)
      .flatMap(cId => clientService.getClient(cId))
      .map(c => c.jobGroups)
      .flatMap(jg => Future{jg.map(x=>x.groupId)})
      .map(jg => generatePublisherFeed(publisherId,jg))
  }
}
