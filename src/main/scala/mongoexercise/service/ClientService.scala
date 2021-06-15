package mongoexercise.service

import com.typesafe.scalalogging.Logger
import mongoexercise.dao.ClientDao
import mongoexercise.model.Client

import scala.concurrent.Future

class ClientService{

  final val logger = Logger(this.getClass.getName)
  implicit val context = scala.concurrent.ExecutionContext.global
  final val clientDao = new ClientDao()

  def getAllClients() = {
    logger.info("Fetching all clients")
    try{
      clientDao.getAllClients()
    }catch{
      case ex : Exception =>
        logger.error("Error while fetching client details")
        throw ex
    }
  }

  def getClient(clientId : String): Future[Client] = {
    logger.info(s"Fetching client : $clientId")
    try{
      clientDao.getClient(clientId)
    }catch{
      case ex : Exception =>
        logger.error("Error while fetching client details")
        throw ex
    }
  }
}
