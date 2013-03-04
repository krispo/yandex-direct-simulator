package dao.squerylorm.test

import dao.squerylorm.AppSchema

import org.specs2.mutable._
import org.specs2.specification._
import org.squeryl.PrimitiveTypeMode._
import dao.squerylorm.test.helpers._
import domain.PositionValue
import dao.squerylorm.SquerylDao

class SquerylDaoSpec extends Specification with AllExpectations {

  /**
   * data simulator
   */
  val dao = new SquerylDao
  val dt_fmt = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd")
  val date = dt_fmt.parseDateTime("2013-01-01")
  val plusMinutes = date.plusMinutes _

  //work with inMemory DB
  "generateNetAdvisedBids(bp, mu, sigma, dt)" should {
    sequential
    "put 5 NetAdvisedBids" in {
      TestDB_0.creating_and_filling_inMemoryDB() {
        inTransaction {
          val bp = AppSchema.bannerphrases.toList.head
          val mu = PositionValue(0.01, 2.00, 2.50, 3.00, 0.20)
          val sigma = PositionValue(0.001, 0.1, 0.1, 0.1)
          val nb = 1 to 5 map (i => dao.generateNetAdvisedBids(bp, mu, sigma, plusMinutes(i)))

          AppSchema.netadvisedbidhistory.toList.length must_== (3 + 5)
        }
      }
    }
  }

  "generateBannerPhrasePerformance(bp, dt)" should {
    sequential
    "put 5 BannerPhrasePerformance elements" in {
      TestDB_0.creating_and_filling_inMemoryDB() {
        inTransaction {
          val c = AppSchema.campaigns.toList.head
          val bp = c.bannerPhrases.head
          val nb = 1 to 5 map (i => dao.generateBannerPhrasePerformance(bp, plusMinutes(i)))

          AppSchema.bannerphraseperformance.toList.length must_== (3 + 5)
        }
      }
    }
  }

  "generateCampaignPerformance(c)" should {
    sequential
    "put 1 campaignPerformance elements" in {
      TestDB_0.creating_and_filling_inMemoryDB() {
        inTransaction {
          val c = dao.getCampaign("krisp0", 1).get
          dao.generateCampaignPerformance(c, plusMinutes(1))

          val cpl = AppSchema.campaignperformance.toList
          cpl.length must_== (1 + 1)
          val cp = cpl.filter(cp => cp.dateTime == plusMinutes(1)).head
          cp.campaign_id must_== (1)
          cp.cost_context must_== (6)
          cp.impress_context must_== (30)
          cp.clicks_context must_== (3)
        }
      }
    }
  }

  "generateBudget(c, dt)" should {
    sequential
    "put 1 budget elements" in {
      TestDB_0.creating_and_filling_inMemoryDB() {
        inTransaction {
          val c = AppSchema.campaigns.toList.head
          dao.generateBudget(c, plusMinutes(1))

          val bh = AppSchema.budgethistory.toList
          bh.length must_== (1 + 1)
          val b = bh.filter(b => b.dateTime == plusMinutes(1)).head
          b.campaign_id must_== (1)
          b.budget must_== (96)
        }
      }
    }
  }

  "fill inMemory DB with generating data" should {
    sequential

    "just create schema and fill DB with single full generating step" in {
      TestDB_0.creating_and_filling_inMemoryDB() {
        inTransaction {
          //generate NetAdvisedBids
          var c = dao.getCampaign("krisp0", 1).get
          var bp = c.bannerPhrases.head

          val mu = PositionValue(0.01, 2.00, 2.50, 3.00, 0.20)
          val sigma = PositionValue(0.001, 0.1, 0.1, 0.1)

          dao.generateNetAdvisedBids(bp, mu, sigma, plusMinutes(1))
          AppSchema.netadvisedbidhistory.toList.length must_== (3 + 1)

          c = dao.getCampaign("krisp0", 1).get
          bp = c.bannerPhrases.head
          bp.netAdvisedBidsHistory.length must_== (2)

          //generate BannerPhrasePerformance
          c = dao.getCampaign("krisp0", 1).get
          bp = c.bannerPhrases.head
          dao.generateBannerPhrasePerformance(bp, plusMinutes(1))
          AppSchema.bannerphraseperformance.toList.length must_== (3 + 1)

          c = dao.getCampaign("krisp0", 1).get
          bp = c.bannerPhrases.head
          bp.performanceHistory.length must_== (2)

          //generate CampaignPerformance
          c = dao.getCampaign("krisp0", 1).get
          dao.generateCampaignPerformance(c, plusMinutes(1))
          AppSchema.campaignperformance.toList.length must_== (1 + 1)

          c = dao.getCampaign("krisp0", 1).get
          c.performanceHistory.length must_== (2)

          //generate Budget
          c = dao.getCampaign("krisp0", 1).get
          dao.generateBudget(c, plusMinutes(1))
          AppSchema.budgethistory.toList.length must_== (1 + 1)

          c = dao.getCampaign("krisp0", 1).get
          c.budgetHistory.length must_== (2)
        }
      }
    }
  }
}