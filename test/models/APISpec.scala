package models

import models._
import dao.squerylorm._

import json_api.Convert._
import org.specs2.mutable._
import org.specs2.specification._
import play.api.libs.json._
import org.joda.time._
import java.text._

import dao.squerylorm.test.helpers.TestDB_1
import org.squeryl.PrimitiveTypeMode._

class APISpec extends Specification with AllExpectations {
  /* Date format */
  val date_fmt = new SimpleDateFormat("yyyy-MM-dd")

  /*------------- GetCampaignsList ---------------------------------------------------*/
  "GetCampaignsList" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").getCampaignsList
          res.length must_== (5)
          res.head.CampaignID must_== (1)
          res.head.Login must_== ("krisp0")
          res.head.Name must_== ("Campaign_0")
          res.head.Rest must_== (41)

          res.last.CampaignID must_== (5)
          res.last.Login must_== ("krisp0")
          res.last.Name must_== ("Campaign_4")
          res.last.Rest must_== (0.0)

        }
      }
    }
  }

  /*------------- GetBanners ---------------------------------------------------*/
  "GetBanners" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").getBanners(GetBannersInfo(List(1)))
          res.length must_== (5)
          res.head.BannerID must_== (1)
          res.head.Text must_== ("Banner_0")
          res.head.Phrases.length must_== (20)
          res.head.Phrases.head.BannerID must_== (1)
          res.head.Phrases.head.CampaignID must_== (1)
          res.head.Phrases.head.Phrase must_== ("Phrase_0")
          res.head.Phrases.head.Min must_== (59.0)
          res.head.Phrases.head.Max must_== (60.0)
          res.head.Phrases.head.PremiumMin must_== (62.0)
          res.head.Phrases.head.PremiumMax must_== (63.0)
          res.head.Phrases.head.Price must_== (62.0)

          res.last.BannerID must_== (5)
          res.last.Text must_== ("Banner_4")
          res.last.Phrases.length must_== (20)
        }
      }
    }
  }

  /*------------- GetSummaryStat ---------------------------------------------------*/
  "GetSummaryStat" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").getSummaryStat(GetSummaryStatRequest(List(1, 2, 3, 4, 5), "2013-01-01", "2013-01-01"))
          res.length must_== (5)
          res.head.CampaignID must_== (1)
          res.head.SumSearch must_== (3660)
          res.head.ShowsSearch must_== (18300)
          res.head.ClicksSearch must_== (1830)

          res.last.CampaignID must_== (5)
        }
      }
    }
  }

  /*------------- UpdatePrices ---------------------------------------------------*/
  "UpdatePrices" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").updatePrices(List(
            PhrasePriceInfo(PhraseID = 1, BannerID = 1, CampaignID = 1, Price = 1.11),
            PhrasePriceInfo(PhraseID = 2, BannerID = 1, CampaignID = 1, Price = 2.11),
            PhrasePriceInfo(PhraseID = 3, BannerID = 1, CampaignID = 1, Price = 3.11),
            PhrasePriceInfo(PhraseID = 21, BannerID = 2, CampaignID = 1, Price = 21.21),
            PhrasePriceInfo(PhraseID = 22, BannerID = 2, CampaignID = 1, Price = 22.21),
            PhrasePriceInfo(PhraseID = 23, BannerID = 2, CampaignID = 1, Price = 23.21)))
          res must_== (false)

          val c = AppSchema.campaigns.toList.head
          BannerPhrase.select(campaign = c, banner_id = 1, phrase_id = 1, region_id = 1).get.actualBidHistory.head.bid must_== (1.11)
          BannerPhrase.select(campaign = c, banner_id = 1, phrase_id = 2, region_id = 1).get.actualBidHistory.head.bid must_== (2.11)
          BannerPhrase.select(campaign = c, banner_id = 1, phrase_id = 3, region_id = 1).get.actualBidHistory.head.bid must_== (3.11)
          BannerPhrase.select(campaign = c, banner_id = 2, phrase_id = 21, region_id = 1).get.actualBidHistory.head.bid must_== (21.21)
          BannerPhrase.select(campaign = c, banner_id = 2, phrase_id = 22, region_id = 1).get.actualBidHistory.head.bid must_== (22.21)
          BannerPhrase.select(campaign = c, banner_id = 2, phrase_id = 23, region_id = 1).get.actualBidHistory.head.bid must_== (23.21)

          BannerPhrase.select(campaign = c, banner_id = 4, phrase_id = 75, region_id = 1).get.actualBidHistory.head.bid must_== (62)
        }
      }
    }
  }

  /*------------- CreateNewReport ---------------------------------------------------*/
  "CreateNewReport" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").createNewReport(
            NewReportInfo(
              CampaignID = 1,
              StartDate = "2013-01-01",
              EndDate = "2013-01-01"))
          res must_== (3)

          AppSchema.reports.toList.length must_== (3)
        }
      }
    }
  }

  /*------------- getReportList ---------------------------------------------------*/
  "GetReportList" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val conf = play.api.Play.current.configuration

          val res = API("krisp0", "token_1").getReportList

          res.length must_== (2)
          res.head.ReportID must_== (1)
          res.head.Url must_== (Some(conf.getString("uri").get + "/report/1"))
          res.head.StatusReport must_== ("Done")
          res.last.Url must_== (Some(conf.getString("uri").get + "/report/2"))
        }
      }
    }
  }

  /*------------- GetXML ---------------------------------------------------*/
  "getXml" should {
    sequential

    "take TRUE data" in {

      import scala.xml._
      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("").getXml(2)
          (res \ "reportID").text must_== ("2")
          (res \ "campaignID").text must_== ("1")
          (res \ "startDate").text must_== ("2013-01-01")
          (res \ "endDate").text must_== ("2013-01-01")

          ((res \ "phrasesDict" \\ "phrase").head \ "@phraseID").text.toLong must_== (1) //(c.bannerPhrases.head.phrase.get.id)
          ((res \ "phrasesDict" \\ "phrase").head \ "@value").text must_== ("Phrase_0") //(c.bannerPhrases.head.phrase.get.phrase)

          val row = (res \\ "row").head
          (row \ "@bannerID").text.toInt must_== (1)
          (row \ "@phraseID").text.toInt must_== (1)
          (row \ "@phrase_id").text.toInt must_== (1)
          (row \ "@sum_search").text.toDouble must_== (3660.0)
          (row \ "@sum_context").text.toDouble must_== (3660.0)
          (row \ "@shows_search").text.toInt must_== (18300)
          (row \ "@shows_context").text.toInt must_== (18300)
          (row \ "@clicks_search").text.toInt must_== (1830)
          (row \ "@clicks_context").text.toInt must_== (1830)
        }
      }
    }
  }

  /*------------- DeleteReport ---------------------------------------------------*/
  "DeleteReport" should {
    sequential

    "delete head report" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").deleteReport(1)
          res must_== (1)
          AppSchema.reports.toList.length must_== (1)
          AppSchema.reports.toList.head.id must_== (2)

          val res2 = API("krisp0", "token_1").deleteReport(5)
          res2 must_== (0)
          AppSchema.reports.toList.length must_== (1)
          AppSchema.reports.toList.head.id must_== (2)
        }
      }
    }

    "delete last report" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").deleteReport(2)
          res must_== (1)
          AppSchema.reports.toList.length must_== (1)
          AppSchema.reports.toList.head.id must_== (1)

          val res2 = API("krisp0", "token_1").deleteReport(5)
          res2 must_== (0)
          AppSchema.reports.toList.length must_== (1)
          AppSchema.reports.toList.head.id must_== (1)
        }
      }
    }
  }

  /*------------- GetBannersStat ---------------------------------------------------*/
  "getBannersStat" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = API("krisp0", "token_1").getBannersStat(
            NewReportInfo(
              CampaignID = 1,
              StartDate = "2013-01-01",
              EndDate = "2013-01-01"))

          res.CampaignID must_== (1)
          res.StartDate must_== ("2013-01-01")
          res.EndDate must_== ("2013-01-01")

          res.Stat.length must_== (100) //5 banners with 20 phrases in each
          res.Stat.head.BannerID must_== (1)
          res.Stat.head.PhraseID must_== (Some(1))
          res.Stat.head.Phrase must_== ("Phrase_0")
          res.Stat.head.SumSearch must_== (3660.0)
          res.Stat.head.SumContext must_== (3660.0)
          res.Stat.head.ShowsSearch must_== (18300)
          res.Stat.head.ShowsContext must_== (18300)
          res.Stat.head.ClicksSearch must_== (1830)
          res.Stat.head.ClicksContext must_== (1830)

        }
      }
    }
  }
}