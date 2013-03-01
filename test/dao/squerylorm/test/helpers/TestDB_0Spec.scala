package dao.squerylorm.test.helpers

import org.specs2.mutable._
import org.specs2.specification._

import org.squeryl.PrimitiveTypeMode._
import dao.squerylorm.AppSchema

class TestDB_0Spec extends Specification with AllExpectations {

  //work with in memory DB
  "fill_memory DB" should {
    sequential

    "fill DB" in {

      TestDB_0.creating_and_filling_inMemoryDB() {
        inTransaction {

          AppSchema.users.toList.length must_== (1)

          val c = AppSchema.campaigns.toList
          c.length must_== (1)
          c.head.name must_== ("Campaign_0")
          c.head.banners.length must_== (1)
          c.head.banners.head.text must_== ("Banner_0")

          AppSchema.campaignperformance.toList.length must_== (1)

          AppSchema.banners.toList.length must_== (1)
          AppSchema.phrases.toList.length must_== (3)
          AppSchema.regions.toList.length must_== (1)

          AppSchema.bannerphrases.toList.length must_== (3)
          AppSchema.bannerphraseperformance.toList.length must_== (3)
          AppSchema.netadvisedbidhistory.toList.length must_== (3)
          AppSchema.actualbidhistory.toList.length must_== (3)
        }
      }
    }
  }
}