package basicexercise

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FuturesExercise extends App {
  //Hint - Try map/for-comprehension on Futures.
  //Use .copy() to make copies of immutable objects in which you want to update data

  case class Job(jobId: String, title: String, clicks: Option[Int] = None, applies: Option[Int] = None)
  case class ClicksStat(jobId: String, clicks: Int)
  case class AppliesStat(jobId: String, applies: Int)

  val jobs = Future.successful(List(Job("job1", "title1"), Job("job2", "title1"), Job("job3", "title3")))
  val clicks = Future.successful(List(ClicksStat("job2", 50)))
  val applies = List(AppliesStat("job3", 150))

  var enJob = jobs.flatMap(jobsList =>
    clicks.flatMap(clicksList =>
      Future[List[Job]] {
        jobsList.map(job =>
          job.copy(clicks = {
            val resClick = clicksList.find(c => c.jobId == job.jobId)
            if (resClick.isDefined)
              Some(resClick.get.clicks)
            else None
          },
            applies = {
              val resApplies = applies.find(a => a.jobId == job.jobId)
              if (resApplies.isDefined)
                Some(resApplies.get.applies)
              else
                None
            }))
      }))

  enJob = for {jobList <- jobs
               clicksList <- clicks}
    yield {
      for { job <- jobList }
        yield job.copy(clicks = {
          val resClick = clicksList.find(c => c.jobId == job.jobId)
          if (resClick.isDefined)
            Some(resClick.get.clicks)
          else None
        },
          applies = {
            val resApplies = applies.find(a => a.jobId == job.jobId)
            if (resApplies.isDefined)
              Some(resApplies.get.applies)
            else
              None
          })
    }

  // If stats are not present clicks/Applies should be None
  // It should contain => Future(List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150))))
  val jobsEnrichedWithStats = Await.result(enJob,5.minutes)
  println(jobsEnrichedWithStats)

  val jobsWithClicksNotNone = jobsEnrichedWithStats.filter(job => job.clicks.isDefined)
  println(jobsWithClicksNotNone)

  val jobsWithAppliesNotNone = jobsEnrichedWithStats.filter(job => job.applies.isDefined)
  println(jobsWithAppliesNotNone)

  //  title -> List[Job]
  val jobsGroupedByTitle = jobsEnrichedWithStats.groupBy(job => job.title)
  println(jobsGroupedByTitle)

  // Should return Map(title -> (sumClicks, sumApplies)). if clicks/applies is None, set its value as 0
  val statsPerTitle = jobsEnrichedWithStats.groupBy(j => j.title).mapValues(jobs => (jobs.filter(_.clicks.isDefined).map(_.clicks.get).sum,
    jobs.filter(_.applies.isDefined).map(_.applies.get).sum))
  println(statsPerTitle)
}
