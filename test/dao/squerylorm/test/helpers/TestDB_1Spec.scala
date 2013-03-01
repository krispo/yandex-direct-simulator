package dao.squerylorm.test.helpers

import models._

import org.specs2.mutable._
import org.specs2.specification._
import org.squeryl.PrimitiveTypeMode._
import dao.squerylorm.AppSchema

class TestDB_1Spec extends Specification with AllExpectations {

  //work with postgreSQL DB
  "fill postgreSQL DB" should {
    sequential

    "just drop and recreate schema and fill DB" in {
      TestDB_1.creating_and_filling_DB()()
      1 must_== (1)
    }
  }

  //work with in memory DB
  "fill_memory DB" should {
    sequential

    "fill DB" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {

          val u = AppSchema.users.toList
          u.length must_== (1)
          u.head.reports.length must_== (2)
          u.head.reports.head.user must_== (Some(u.head))
          u.head.reports.head.content must be equalTo (NewReportInfo(1, "2013-01-01", "2013-01-01").toXML.toString)

          val c = AppSchema.campaigns.toList
          c.length must_== (5)
          c.head.name must_== ("Campaign_0")
          c.head.banners.length must_== (5)
          c.head.banners.head.text must_== ("Banner_0")

          val bh = AppSchema.budgethistory.toList
          bh.length must_== (60)
          bh.head.budget must_== (100)
          bh.last.budget must_== (41)
          bh.head.campaign_id must_== (1)

          AppSchema.banners.toList.length must_== (5)
          AppSchema.phrases.toList.length must_== (100)
          AppSchema.regions.toList.length must_== (1)

          AppSchema.bannerphrases.toList.length must_== (100)
          AppSchema.bannerphraseperformance.toList.length must_== (6000)

          val cperf = AppSchema.campaignperformance.toList
          cperf.length must_== (60)
          cperf.head.cost_search must_== (2)
          cperf.head.impress_search must_== (10)
          cperf.head.clicks_search must_== (1)

          val netAdvBid = AppSchema.netadvisedbidhistory.toList
          netAdvBid.length must_== (6000)
          netAdvBid.head.a must_== (0)
          netAdvBid.head.b must_== (1)
          netAdvBid.head.c must_== (3)
          netAdvBid.head.d must_== (4)

          val ab = AppSchema.actualbidhistory.toList
          ab.length must_== (6000)
          ab.head.bid must_== (3)

        }
      }
    }
  }
}

