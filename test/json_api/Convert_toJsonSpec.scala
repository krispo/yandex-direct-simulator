package json_api

import models._

import json_api.Convert._
import org.specs2.mutable._
import org.specs2.specification._
import play.api.libs.json._
import org.joda.time._

import java.text._

class Convert_toJsonSpec extends Specification with AllExpectations {

  /* Date format */
  val date_fmt = new SimpleDateFormat("yyyy-MM-dd")

  /*------------- ShortCampaignInfo ---------------------------------------------------*/
  "toJson - ShortCampaignInfo" should {
    sequential

    "take TRUE data" in {
      val date = date_fmt.parse("2013-01-01")

      val data = ShortCampaignInfo(
        CampaignID = 100,
        Login = "krisp0",
        Name = "some_name",
        StartDate = new DateTime(date),
        Sum = 10.3,
        Rest = 20.1,
        SumAvailableForTransfer = Some(30.0),
        Shows = 100,
        Clicks = 10,
        Status = Some(""),
        StatusModerate = None,
        IsActive = Some("yes"),
        ManagerName = Some("manager"),
        AgencyName = None)

      val res = toJson[ShortCampaignInfo](data)

      res \ "CampaignID" must_== (JsNumber(100))
      res \ "Login" must_== (JsString("krisp0"))
      res \ "Sum" must_== (JsNumber(10.3))
      (res \ "Status").asOpt[String] must_== (Some(""))
      (res \ "StartDate").as[DateTime] must_== (new DateTime(date))
      (res \ "ManagerName").asOpt[String] must_== (Some("manager"))
      (res \ "AgencyName").asOpt[String] must_== (None)

    }
  }

  /*------------- List[ShortCampaignInfo] ---------------------------------------------------*/
  "toJson - List[ShortCampaignInfo]" should {
    sequential

    "take TRUE data" in {
      val date = date_fmt.parse("2013-01-01")
      val sci = ShortCampaignInfo(
        CampaignID = 100,
        Login = "krisp0",
        Name = "some_name",
        StartDate = new DateTime(date),
        Sum = 10.3,
        Rest = 20.1,
        SumAvailableForTransfer = Some(30.0),
        Shows = 100,
        Clicks = 10,
        Status = Some(""),
        StatusModerate = None,
        IsActive = Some("yes"),
        ManagerName = Some("manager"),
        AgencyName = None)
      val data = List(sci, sci)

      val res = toJson[List[ShortCampaignInfo]](data)

      res \\ "CampaignID" map (_.as[Int]) must_== (List(100, 100))
      res \\ "Login" map (_.as[String]) must_== (List("krisp0", "krisp0"))
      res \\ "Sum" must_== (List(JsNumber(10.3), JsNumber(10.3)))
      res \\ "ManagerName" map (_.asOpt[String]) must_== (List(Some("manager"), Some("manager")))
      res \\ "AgencyName" must_== (List())

    }
  }

  /*------------- BannerInfo ---------------------------------------------------*/
  "toJson - BannerInfo" should {
    sequential

    "take TRUE data" in {
      val bphi = BannerPhraseInfo(Phrase = "some_phrase")
      val data = BannerInfo(
        BannerID = 100,
        Text = "some",
        Geo = "12, 11",
        Phrases = List(bphi, bphi))

      val res = toJson[BannerInfo](data)

      res \ "BannerID" must_== (JsNumber(100))
      res \ "Text" must_== (JsString("some"))
      (res \ "Geo").as[String] must_== ("12, 11")
      res \ "Phrases" \\ "CampaignID" must_== (List(JsNumber(0), JsNumber(0)))
      res \ "Phrases" \\ "Phrase" map (_.as[String]) must_== (List("some_phrase", "some_phrase"))
    }
  }

  /*------------- List[BannerInfo] ---------------------------------------------------*/
  "toJson - List[BannerInfo]" should {
    sequential

    "take TRUE data" in {
      val bphi = BannerPhraseInfo(Phrase = "some_phrase")
      val bi = BannerInfo(
        BannerID = 100,
        Text = "some",
        Geo = "12, 11",
        Phrases = List(bphi, bphi))
      val data = List(bi, bi)

      val res = toJson[List[BannerInfo]](data)

      res \\ "Text" map (_.as[String]) must_== (List("some", "some"))
      (res \\ "Phrases").head \\ "Phrase" map (_.as[String]) must_== (List("some_phrase", "some_phrase"))
    }
  }

  /*------------- StatItem ---------------------------------------------------*/
  "toJson - StatItem" should {
    sequential

    "take TRUE data" in {
      val data = StatItem(
        SumSearch = 1.0,
        SumContext = 2.0,
        ShowsSearch = 10,
        ClicksContext = 40)

      val res = toJson[StatItem](data)
      res \ "SumSearch" must_== (JsNumber(1.0))
      res \ "ClicksContext" must_== (JsNumber(40))
    }
  }

  /*------------- List[StatItem] ---------------------------------------------------*/
  "toJson - List[StatItem]" should {
    sequential

    "take TRUE data" in {
      val data = List(
        StatItem(
          SumSearch = 1.0,
          SumContext = 2.0,
          ShowsSearch = 10,
          ClicksContext = 40),
        StatItem())

      val res = toJson[List[StatItem]](data)

      res \\ "SumSearch" must_== (List(JsNumber(1.0), JsNumber(0.0)))
      res \\ "ClicksContext" map (_.as[Int]) must_== (List(40, 0))
    }
  }

  /*------------- ReportInfo ---------------------------------------------------*/
  "toJson - ReportInfo" should {
    sequential

    "take TRUE data" in {
      val data = ReportInfo(
        ReportID = 100,
        StatusReport = "Pending")

      val res = toJson[ReportInfo](data)

      res \ "ReportID" must_== (JsNumber(100))
      (res \ "Url").asOpt[String] must_== (None)
      res \ "StatusReport" must_== (JsString("Pending"))
    }
  }

  /*------------- List[ReportInfo] ---------------------------------------------------*/
  "toJson - List[ReportInfo]" should {
    sequential

    "take TRUE data" in {
      val data = List(
        ReportInfo(
          ReportID = 100,
          StatusReport = "Pending"),
        ReportInfo(
          ReportID = 200,
          Url = Some("https://some.address"),
          StatusReport = "Done"))

      val res = toJson[List[ReportInfo]](data)

      res \\ "ReportID" must_== (List(JsNumber(100), JsNumber(200)))
      res \\ "Url" map (_.asOpt[String]) must_== (List(Some("https://some.address")))
      res \\ "StatusReport" map (_.as[String]) must_== (List("Pending", "Done"))
    }
  }

  /*------------- GetBannersStatResponse ---------------------------------------------------*/
  "toJson - GetBannersStatResponse" should {
    sequential

    "take TRUE data" in {
      val date = date_fmt.parse("2013-01-01")

      val bsi = BannersStatItem(
        BannerID = 11,
        PhraseID = Some(1),
        Phrase = "some",
        Sum = 12.3,
        Clicks = 50,
        Shows = 550)

      val data = GetBannersStatResponse(
        CampaignID = 10,
        StartDate = "2013-01-01",
        EndDate = "2013-01-01",
        Stat = List(bsi, bsi))

      val res = toJson[GetBannersStatResponse](data)

      res \ "CampaignID" must_== (JsNumber(10))
      res \ "StartDate" must_== (JsString("2013-01-01"))
      res \ "Stat" \\ "BannerID" must_== (List(JsNumber(11), JsNumber(11)))
      //res \ "Stat" \\ "PhraseID" must_== (List(JsNumber(1), JsNumber(1)))
      res \ "Stat" \\ "Phrase" must_== (List(JsString("some"), JsString("some")))
      res \ "Stat" \\ "Sum" must_== (List(JsNumber(12.3), JsNumber(12.3)))
    }
  }
}