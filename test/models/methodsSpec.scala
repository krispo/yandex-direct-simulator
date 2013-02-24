package models

import models._

import json_api.Convert._
import org.specs2.mutable._
import org.specs2.specification._
import play.api.libs.json._
import org.joda.time._
import java.text._

import dao.squerylorm.test.helpers.TestDB_1
import org.squeryl.PrimitiveTypeMode._

class methodsSpec extends Specification with AllExpectations {
  /* Date format */
  val date_fmt = new SimpleDateFormat("yyyy-MM-dd")

  /*------------- GetCampaignsList ---------------------------------------------------*/
  "ShortCampaignInfo" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = ShortCampaignInfo.get("krisp0", "token_1")
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
  "BannerInfo" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = BannerInfo.get("krisp0", "token_1", GetBannersInfo(List(1)))
          res.length must_== (5)
          res.head.BannerID must_== (1)
          res.head.Text must_== ("Banner_0")
          res.head.Phrases.length must_== (20)
          res.head.Phrases.head.BannerID must_== (1)
          res.head.Phrases.head.CampaignID must_== (1)
          res.head.Phrases.head.Phrase must_== ("Phrase_0")
          res.head.Phrases.head.Min must_== (0)
          res.head.Phrases.head.Max must_== (1)
          res.head.Phrases.head.PremiumMin must_== (3)
          res.head.Phrases.head.PremiumMax must_== (4)
          res.head.Phrases.head.Price must_== (3)

          res.last.BannerID must_== (5)
          res.last.Text must_== ("Banner_4")
          res.last.Phrases.length must_== (20)
        }
      }
    }
  }

  /*------------- GetSummaryStat ---------------------------------------------------*/
  "StatItem" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val res = StatItem.get("krisp0", "token_1", GetSummaryStatRequest(List(1, 2, 3, 4, 5), "2013-01-01", "2013-01-01"))
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
}