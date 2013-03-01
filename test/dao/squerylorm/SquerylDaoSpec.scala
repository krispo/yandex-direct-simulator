package dao.squerylorm

import org.specs2.mutable._
import org.specs2.specification._
import org.squeryl.PrimitiveTypeMode._

import dao.squerylorm.test.helpers._

class SquerylDaoSpec extends Specification with AllExpectations {

  /**
   * data simulator
   */
  val dao = new SquerylDao

  "generateNetAdvisedBids(bp, mu, sigma, dt)" should {
    sequential
    "get 2 Campaigns from TestDB_0" in {
      TestDB_0.creating_and_filling_inMemoryDB() {
                
      }
    }
  }
}