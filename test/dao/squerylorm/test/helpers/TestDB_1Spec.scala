package dao.squerylorm.test.helpers

import org.specs2.mutable._
import org.specs2.specification._
import play.api.test._
import play.api.test.Helpers._
import org.squeryl.PrimitiveTypeMode._
import dao.squerylorm._
import dao.Dao

class TestDB_1Spec extends Specification with AllExpectations {

  "fill_DB" should {
    sequential

    //work with postgreSQL DB
    TestDB_1.creating_and_filling_DB()()

    //work with in memory DB
    "put User" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {

          AppSchema.users.toList.length must_== (1)
          AppSchema.campaigns.toList.length must_== (5)

          AppSchema.banners.toList.length must_== (5)
          AppSchema.phrases.toList.length must_== (100)
          AppSchema.regions.toList.length must_== (1)

          AppSchema.bannerphrases.toList.length must_== (100)

          val cperf = AppSchema.campaignperformance.toList
          cperf.length must_== (60)
          cperf.head.cost_search must_== (2)
          cperf.head.impress_search must_== (10)
          cperf.head.clicks_search must_== (1)          

        }
      }
    }
  }
}

