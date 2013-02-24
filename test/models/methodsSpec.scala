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

  /*------------- ShortCampaignInfo ---------------------------------------------------*/
  "BannerInfo" should {
    sequential

    "take TRUE data" in {

      TestDB_1.creating_and_filling_inMemoryDB() {
        inTransaction {
          val bil = BannerInfo.get("krisp0", "token_1", GetBannersInfo(List(1)))
          bil.length must_== (5)
          bil.head.BannerID must_== (1)
          bil.head.Text must_== ("Banner_0")
          bil.head.Phrases.length must_== (20)
          bil.head.Phrases.head.BannerID must_== (1)
          bil.head.Phrases.head.CampaignID must_== (1)
          bil.head.Phrases.head.Phrase must_== ("Phrase_0")
          bil.head.Phrases.head.Min must_== (0)
          bil.head.Phrases.head.Max must_== (1)
          bil.head.Phrases.head.PremiumMin must_== (3)
          bil.head.Phrases.head.PremiumMax must_== (4)
          bil.head.Phrases.head.Price must_== (3)
          
          bil.last.BannerID must_== (5)
          bil.last.Text must_== ("Banner_4")
          bil.last.Phrases.length must_== (20)

          //AppSchema.bannerphrases.toList.length must_== (100)          
        }
      }

    }
  }
}