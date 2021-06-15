package mongoexercise.server


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import mongoexercise.model.Job
import mongoexercise.service.{ClientService, JobGroupService, JobService, PublisherService}
import mongoexercise.util.JsonHelper

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Failure, Success}


object WebServer extends App with JsonHelper {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val context: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global
  val jobService: JobService = new JobService()
  val jobGroupService: JobGroupService = new JobGroupService()
  val clientService: ClientService = new ClientService()
  val publisherService: PublisherService = new PublisherService()

  val appRoute =
    pathPrefix("api") {
      get {
        pathPrefix("jobs") {
          path("getAllJobs") {
            parameters("pgNo".as[Int], "limit".as[Int]) {
              (pgNo, limit) => {
                onComplete(jobService.getJobsWithPagination(pgNo, limit)) {
                  case Success(value) =>
                    complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
                  case Failure(ex) =>
                    print(s"Failed with exception $ex")
                    complete(StatusCodes.InternalServerError)
                }
              }
            }
          }
        } ~ pathPrefix("jobGroups") {
          path("getAllJobGroups") {
            onComplete(jobGroupService.getAllJobGroups) {
              case Success(value) =>
                complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
              case Failure(ex) =>
                print(s"Failed with exception $ex")
                complete(StatusCodes.InternalServerError)
            }
          } ~ path("getJobGroup" / Segment) { (jobGroup) => {
            onComplete(jobGroupService.getJobGroup(jobGroup)) {
              case Success(value) =>
                complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
              case Failure(ex) =>
                print(s"Failed with exception $ex")
                complete(StatusCodes.InternalServerError)
            }
          }
          } ~ path(Segment / "getJobs") {
            jobGroupId => {
              onComplete(jobGroupService.getAllJobs(jobGroupId)) {
                case Success(value) =>
                  complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
                case Failure(ex) =>
                  print(s"Failed with exception $ex")
                  complete(StatusCodes.InternalServerError)
              }
            }
          }
        } ~ pathPrefix("clients") {
          path("getAllClients") {
            onComplete(clientService.getAllClients()) {
              case Success(value) =>
                complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
              case Failure(ex) =>
                print(s"Failed with exception $ex")
                complete(StatusCodes.InternalServerError)
            }
          } ~ path("getClient" / Segment) { (clientId) => {
            onComplete(clientService.getClient(clientId)) {
              case Success(value) =>
                complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
              case Failure(ex) =>
                print(s"Failed with exception $ex")
                complete(StatusCodes.InternalServerError)
            }
          }
          }
        } ~ pathPrefix("publishers") {
          path("getAllPublishers") {
            onComplete(publisherService.getAllPublisher()) {
              case Success(value) =>
                complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
              case Failure(ex) =>
                print(s"Failed with exception $ex")
                complete(StatusCodes.InternalServerError)
            }
          } ~ path("getPublisher" / Segment) { (publisherId) => {
            onComplete(publisherService.getPublisher(publisherId)) {
              case Success(value) =>
                complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, write(value))))
              case Failure(ex) =>
                print(s"Failed with exception $ex")
                complete(StatusCodes.InternalServerError)
            }
          }
          }~ path("generatePublisherFeed" / Segment) { (publisherId) => {
            try {
              publisherService.generateFeed(publisherId)
              complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, "Feed generation is success")))
            } catch {
              case ex: Exception =>
                print(s"Exception Occurred $ex")
                complete(StatusCodes.InternalServerError)
            }
          }
          }
        }
      } ~ post {
        pathPrefix("jobs") {
          path("addJob") {
            entity(as[String]) { (jobJson) => {
              val newJob: Job = parse(jobJson).extract[Job]
              onComplete(jobService.addJob(newJob)) {
                case Success(value) =>
                  complete(StatusCodes.OK, "Client creation successful")
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError, "Client Creation Failed")
              }
            }
            }
          }
        }
      } ~ put {
        reject
      } ~ delete {
        pathPrefix("jobs") {
          path("deleteJob" / Segment) {
            (jobId: String) => {
              onComplete(jobService.deleteJob(jobId)) {
                case Success(value) =>
                  complete(StatusCodes.OK, "Client deletion successful")
                case Failure(ex) =>
                  complete(StatusCodes.InternalServerError, "Client Creation Failed")
              }
            }
          }
        }
      }
    }

  val port = 8080
  val serverFuture = Http().bindAndHandle(appRoute, "localhost", port)
  println(s"Server is online at port = $port, PRESS ENTER TO EXIT")
  StdIn.readLine()
  serverFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}


