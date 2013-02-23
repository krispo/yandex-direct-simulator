package dao.squerylorm.test.helper

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import play.api.test._
import play.api.test.Helpers._
import org.joda.time
import java.sql.Timestamp
import scala.reflect._
import dao.squerylorm._

object TestDB_1 extends AppHelpers {

  def fill_DB() = {

    //Users
    val user = User("User_1", "123").put

  }

}