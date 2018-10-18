import Main.{db, peopleTable}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


object Misc {
  def count = {
    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(peopleTable.size.result).map( {
        case num => println(s"There are $num people in the database")
      })
    }
    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }
  }

  def avgAge = {
    val q1 = for {
      c <- peopleTable
    } yield (c.age)
    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q1.avg.result).map( {
        case num => println(s"The average age is $num")
      })
    }
    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }
  }
//
//  def commonName={
//    val q1 = (for {
//      c <- peopleTable
//    } yield (c)).groupBy(_.fName)
//    val queryFuture = Future {
//      //simple query that selects everything from People and prints them out
//      db.run(q1.result).map( {
//        case (_,_,_,_,_) =>
//      })
//    }
//    Await.result(queryFuture, Duration.Inf).andThen {
//      case Success(_) =>  db.close()  //cleanup DB connection
//      case Failure(error) =>
//        println("Listing people failed due to: " + error.getMessage)
//    }
//  }


  def commonName={
   val q = (for {
     p <- peopleTable
   } yield (p)).groupBy(_.fName).map { case(a,b) => a -> b.length}

    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q.result).map( {
        case a => println(a.maxBy(a => a._2))
      })
    }

    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }
  }

  def commonLastName={
    val q = (for {
      p <- peopleTable
    } yield (p)).groupBy(_.lName).map { case(a,b) => a -> b.length}

    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q.result).map( {
        case a => println(a.maxBy(a => a._2))
      })
    }

    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }
  }

  def neighbour()={
    val q = (for {
      p <- peopleTable
    } yield (p)).groupBy(_.streetName).map { case(a,b) => a -> b.length}

    val queryFuture = Future {
      //simple query that selects everything from People and prints them out
      db.run(q.result).map( {
        case a  if a.maxBy(a => a._2)._2 > 1 => println("There are neighbours")
        case _ => println("There are no neighbours in the table")
      })
    }

    Await.result(queryFuture, Duration.Inf).andThen {
      case Success(_) =>  db.close()  //cleanup DB connection
      case Failure(error) =>
        println("Listing people failed due to: " + error.getMessage)
    }
  }
}
