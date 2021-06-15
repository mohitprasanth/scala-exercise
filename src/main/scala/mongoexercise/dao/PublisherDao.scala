package mongoexercise.dao

import mongoexercise.common.{Constants, DBProperties}
import mongoexercise.dao.PublisherDao.publisherCollection
import mongoexercise.model.Publisher
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{MongoCollection, MongoDatabase}


object PublisherDao {
  val db: MongoDatabase = DBProperties.database
  private val publisherCollection: MongoCollection[Publisher] = db.getCollection[Publisher](Constants.PUBLISHER_COLLECTION)
}

class PublisherDao {

  def getPublisher(publisherId : String) = {
    publisherCollection.find(equal("publisherId",publisherId)).headOption()
  }

  def getAllPublishers() = {
    publisherCollection.find().toFuture()
  }

}
