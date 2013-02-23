package dao.squerylorm.test.helpers

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import play.api.test._
import play.api.test.Helpers._
import dao.squerylorm._
import scala.util.control.Exception._

trait AppHelpers {

  /**
   * creates in memory DB and schema
   * runs block of code
   * @param block of code
   * @return T
   */
  def creating_and_filling_inMemoryDB[T]()(block: => T): T = {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      inTransaction {
        AppSchema.create
        fill_DB
      }
      block
    }
  }

  /**
   * drop and creates schema in postgreSQL DB
   */
  def creating_and_filling_DB[T]()(block: => T): T = {
    running(FakeApplication()) {
      inTransaction {
        allCatch opt AppSchema.drop
        allCatch opt AppSchema.create
        fill_DB
      }
      block
    }
  }
  
  /**
   * fills DB
   */
  def fill_DB(): Unit
}
