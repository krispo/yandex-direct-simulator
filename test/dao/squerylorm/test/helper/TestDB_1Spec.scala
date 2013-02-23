package dao.squerylorm.test.helpers

import org.specs2.mutable._
import org.specs2.specification._
import play.api.test._
import play.api.test.Helpers._
import org.squeryl.PrimitiveTypeMode._
import dao.squerylorm._
import dao.Dao
import dao.squerylorm.test.helper.TestDB_1

class TestDB_1Spec extends Specification with AllExpectations {

  "fill_DB" should {
    sequential

    "put User" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {

          AppSchema.users.toList.length must_== (1)

        }
      }
    }
  }
}

