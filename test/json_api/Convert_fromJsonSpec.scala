package json_api

import models._

import json_api.Convert._
import org.specs2.mutable._
import org.specs2.specification._
import play.api.libs.json._
import org.joda.time._

class Convert_fromJsonSpec extends Specification with AllExpectations {

  /*------------- GetBannersInfo ---------------------------------------------------*/
  "fromJson - GetBannersInfo" should {
    sequential

    "take TRUE data" in {
      val data = """
       {"CampaignIDS" : [1, 2, 3],
        "GetPhrases" : "WithPrices"}
        """

      val res = fromJson[GetBannersInfo](Json.parse(data)).get

      res.CampaignIDS must_== (List(1, 2, 3))
      res.GetPhrases must_== ("WithPrices")
    }
  }

  /*------------- GetSummaryStatRequest ---------------------------------------------------*/
  "fromJson - GetSummaryStatRequest" should {
    sequential

    "take TRUE data" in {
      val data = """
       {"CampaignIDS" : [1, 2, 3],
        "StartDate" : "2013-01-01",
        "EndDate" : "2013-01-01"}
        """

      val res = fromJson[GetSummaryStatRequest](Json.parse(data)).get

      res.CampaignIDS.must_==(List(1, 2, 3))
      res.StartDate must_== ("2013-01-01")
    }
  }

  /*------------- NewReportInfo ---------------------------------------------------*/
  "fromJson - NewReportInfo" should {
    sequential

    "take TRUE data" in {
      val data = """
       {"CampaignID" : 100,
        "StartDate" : "2013-01-01",
        "EndDate" : "2013-01-01",
        "GroupByColumns" : ["clBanner", "clPhrase"]}
        """

      val res = fromJson[NewReportInfo](Json.parse(data)).get

      res.CampaignID must_== (100)
      res.GroupByColumns must_== (List("clBanner", "clPhrase"))
    }
  }

  /*------------- PhrasePriceInfo ---------------------------------------------------*/
  "fromJson - PhrasePriceInfo" should {
    sequential

    "take TRUE data" in {
      val data = """
       {"PhraseID": 1,
        "BannerID": 2,
        "CampaignID": 3,
        "Price": 4}"""

      val Some(res) = fromJson[PhrasePriceInfo](Json.parse(data))
      res.PhraseID must_== (1)
      res.BannerID must_== (2)
      res.AutoBroker must_== (None)
    }
  }
  /*------------- List[PhrasePriceInfo] ---------------------------------------------------*/
  "fromJson - List[PhrasePriceInfo]" should {
    sequential

    "take TRUE data" in {
      val data = """[
       {"PhraseID": 1,
        "BannerID": 2,
        "CampaignID": 3,
        "Price": 4},
       {"PhraseID": 6,
        "BannerID": 7,
        "CampaignID": 8,
        "Price": 9,
        "AutoBroker": "Yes"}
        ]"""

      val Some(res) = fromJson[List[PhrasePriceInfo]](Json.parse(data))
      res.head.PhraseID must_== (1)
      res.head.Price must_== (4)
      res.head.AutoBroker must_== (None)
      res.last.AutoBroker must_== (Some("Yes"))
    }
  }
}