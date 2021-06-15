package mongoexercise

import mongoexercise.service.JobService
import mongoexercise.util.CommonUtils

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object MongoExercise extends App
{
  val jobService : JobService =  new JobService
  //just a check to eliminate unfortunate runs, so that duplicate data is not populated

  val paginatedJobs = jobService.getJobsWithPagination(10,10);
  val result2 = Await.result(paginatedJobs, 2 minutes)
  println(result2)

  val run = false

  if(run) {
    val jobsFromFile = CommonUtils.fetchJobsFromJson("D:\\kalahast\\IdeaProjects\\ScalaExercise\\src\\main\\resources\\jobs.json")
    val res = jobService.addJobs(jobsFromFile)
    val result = Await.result(res, 2 minutes)
    println(res)

    jobService.getAllJobs().onComplete {
      case Success(res) => println(res)
      case Failure(exception) => println(s"getting exception $exception")
    }
    Thread.sleep(2000)

    val job1 = Await.result(jobService.getJob("JOB1"), 2 minutes).get.copy(company = "newCompany")
    jobService.updateJob("JOB1", job1)

    Thread.sleep(2000)

    jobService.getJob("JOB1").onComplete {
      case Success(res) => println(res)
      case Failure(exception) => println(s"getting exception $exception")
    }
    Thread.sleep(2000)

    jobService.deleteJob("JOB1")
  }

}