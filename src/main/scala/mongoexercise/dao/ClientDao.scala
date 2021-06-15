package mongoexercise.dao

import mongoexercise.common.{Constants, DBProperties}
import mongoexercise.dao.ClientDao.{clientCollection, jobGroupsDao}
import mongoexercise.model.Client
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{MongoCollection, MongoDatabase}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object ClientDao {
  val db: MongoDatabase = DBProperties.database
  private val clientCollection: MongoCollection[Client] = db.getCollection[Client](Constants.CLIENT_COLLECTION)
  private val jobGroupsDao = new JobGroupsDao()
}

class ClientDao {
    def getClient(clientId: String): Future[Client] = {
      for{
        client <- clientCollection.find(equal("clientId",clientId)).headOption() if client.isDefined
        jobGroup <- jobGroupsDao.getJobGroups(client.get.jobGroups.map(x=>x.groupId))
      }yield client.get.copy(jobGroups = jobGroup.toList)

    }

    def getAllClients() = {
      clientCollection.find().toFuture()
    }
}

