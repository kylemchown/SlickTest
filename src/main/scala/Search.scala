import Main.{db, peopleTable}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}



object Search {


  def searchAge(age : Int) = {

    val q1 = for {
      c <- peopleTable if c.age > age
    } yield (c.id, c.fName, c.lName, c.age)

    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q1.result).map(_.foreach {
        case (id, fName, lName, age) => println(s" $id $fName $lName $age")
      })
    }
    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }

  }

  def searchFName(fName : String) = {

    val q1 = for {
      c <- peopleTable if c.fName === fName
    } yield (c.id, c.fName, c.lName, c.age)

    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q1.result).map(_.foreach {
        case (id, fName, lName, age) => println(s" $id $fName $lName $age")
      })
    }
    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }
  }

  def searchlName(lName: String) = {

    val q1 = for {
      c <- peopleTable if c.lName === lName
    } yield (c.id, c.fName, c.lName, c.age)

    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q1.result).map(_.foreach {
        case (id, fName, lName, age) => println(s" $id $fName $lName $age")
      })
    }
    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }

  }



}

