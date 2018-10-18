import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {

  val db = Database.forConfig("mysqlDB")

  val peopleTable = TableQuery[People]

  val dropPeopleCmd = DBIO.seq(peopleTable.schema.drop)
  val initPeopleCmd = DBIO.seq(peopleTable.schema.create)

  def dropDB = {
    //do a drop followed by initialisePeople
    val dropFuture = Future{db.run(dropPeopleCmd)}
    //Attempt to drop the table, Await does not block here
    Await.result(dropFuture, Duration.Inf).andThen{
      case Success(_) =>  initialisePeople
      case Failure(error) =>
        println("Dropping the table failed due to: " + error.getMessage)
        initialisePeople
    }
  }

  def initialisePeople = {
    //initialise people
        val setupFuture =  Future {
          db.run(initPeopleCmd)
        }
    // once our DB has finished initializing we are ready to roll, Await does not block
        Await.result(setupFuture, Duration.Inf).andThen{
          case Success(_) => runQuery
          case Failure(error) =>
            println("Initialising the table failed due to: " + error.getMessage)
        }
  }

  def runQuery = {
    val insertPeople = Future {
      val query = peopleTable ++= Seq(
        (50, "Tum", "Blue", 41, "A Lane"),
    (10, "Tem", "Red", 36, "B Road"),
    (20, "Tim", "Orange", 24, "C Lane"),
        (30, "Tam", "Yellow", 6,"D Lane"),
        (40, "Tam", "Green", 52, "E Road"),
        (50, "Tum", "Blue", 41, "F Lane"),
          (50, "Tum", "Blue", 41, "G Road"),
          (50, "Tum", "Blue", 41, "s Road")
    )
     //insert into `PEOPLE` (`PER_FNAME`,`PER_LNAME`,`PER_AGE`)  values (?,?,?)
        println(query.statements.head) // would print out the query one line up
        db.run(query)
      }
      Await.result(insertPeople, Duration.Inf).andThen {
        case Success(_) => listPeople
        case Failure(error) => println("Welp! Something went wrong! " + error.getMessage)
      }
     }


  def listPeople = {
    val queryFuture = Future {
       //simple query that selects everything from People and prints them out
          db.run(peopleTable.result).map(_.foreach {
            case (id, fName, lName, age, streetName) => println(s" $id $fName $lName $age $streetName")
          })
        }
        Await.result(queryFuture, Duration.Inf).andThen {
          case Success(_) =>  db.close()  //cleanup DB connection
          case Failure(error) =>
            println("Listing people failed due to: " + error.getMessage)
        }
       }

  val q1 = for {
    c <- peopleTable if c.age > 30
  } yield (c.fName)

//dropDB
Misc.commonName
  Misc.commonLastName
  Misc.neighbour()
  Thread.sleep(10000)






}


