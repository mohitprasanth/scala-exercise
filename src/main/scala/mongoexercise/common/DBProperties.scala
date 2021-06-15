package mongoexercise.common

import mongoexercise.model._
import org.bson.codecs.configuration.CodecRegistries._
import org.bson.codecs.configuration.{CodecRegistries, CodecRegistry}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonWriter}
import org.joda.time.DateTime
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoDatabase}

object DBProperties {
  val dbConnection = "mongodb://localhost"
  val dbName = "joveo"

  private val customCodecs = fromProviders(
    classOf[Job],
    classOf[Client],
    classOf[JobGroup],
    classOf[Publisher],
    classOf[Rule]
  )

  protected val customCodecRegistry: CodecRegistry = CodecRegistries.fromCodecs(new JodaDateTimeCodec)
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Rule]),customCodecs, DEFAULT_CODEC_REGISTRY, customCodecRegistry)

  private val mongoClient: MongoClient = MongoClient(dbConnection)
  val database: MongoDatabase = mongoClient.getDatabase(dbName).withCodecRegistry(codecRegistry)

  class JodaDateTimeCodec extends Codec[DateTime] {
    override def encode(writer: BsonWriter, value: DateTime, encoderContext: EncoderContext): Unit = {
      writer.writeDateTime(value.getMillis)
    }

    override def getEncoderClass: Class[DateTime] = classOf[DateTime]

    override def decode(reader: BsonReader, decoderContext: DecoderContext): DateTime = {
      new DateTime(reader.readDateTime())
    }
  }

}
