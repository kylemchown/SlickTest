import Main.{db, peopleTable}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


object Update {

  def updateAgeByName(selectName : String, newAge : Int) = {

    val q1 = for {
      c <- peopleTable if c.fName === selectName
    } yield (c.age)

    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q1.update(newAge))
      }

    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }

  }

}
