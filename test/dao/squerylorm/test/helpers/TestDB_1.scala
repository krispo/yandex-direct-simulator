package dao.squerylorm.test.helpers

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import play.api.test._
import play.api.test.Helpers._
import org.joda.time
import java.sql.Timestamp
import scala.reflect._
import dao.squerylorm._
import scala.xml._

object TestDB_1 extends AppHelpers {
  /*
   * 1 user
   * 5 campaigns
   * 5 banners
   * 100 phrases
   * 1 region
   * 100 bannerphrases
   * 60 timestamps
   */

  def fill_DB() = {
    val midnnight_formatter = time.format.DateTimeFormat.forPattern("yyyy-MM-dd")
    //set start_date
    val date = midnnight_formatter.parseDateTime("2013-01-01")
    // partially applied start_date
    val plusDays = date.plusDays _
    //def get_from_standart_string_DateTime(date: DateTime): String = date.toString(app_formatter)
    val plusMinutes = date.plusMinutes _

    //create user
    val user = User("krisp0", "123").put

    //create campaigns
    val campaigns = 0 until 5 map (i =>
      user.campaignsRel.associate(
        Campaign(user.id, name = "Campaign_" + i.toString, _login = "krisp0", _token = "token_1"))) toList

    val reports = 0 until 2 map (_ => user.reportsRel.associate(Report(user.id, (<a>Hello!</a>).toString))) toList

    //BudgetHistory
    val budgetHistory = 0 until 60 map { i =>
      campaigns(0).budgetHistoryRel.associate(
        BudgetHistory(campaign_id = 1, date = plusMinutes(i), budget = 100 - i))
    } toList

    // create Banners to campaigns(0)
    val banners = 0 until 5 map (i => Banner("Banner_" + i.toString).put) toList

    //Phrases
    val phrases = 0 until 100 map (i => Phrase("Phrase_" + i.toString).put) toList

    //Regions
    val region = Region(description = "Russia").put

    //BannerPhrase
    val bannerPhrases =
      for (i <- 0 until 5; j <- 0 until 20)
        yield BannerPhrase(campaigns.head.id,
        banner_id = banners(i).id,
        phrase_id = phrases((i * 20) + j).id,
        region_id = region.id).put

    //CampaignPerformance
    def createCampaignPerformance(ts: Timestamp, i: Int): CampaignPerformance = {
      CampaignPerformance(
        campaign_id = campaigns.head.id,
        //periodtype_id = periodTypes(0).id,
        cost_search = 2 * i,
        cost_context = 2 * i,
        impress_search = 10 * i,
        impress_context = 10 * i,
        clicks_search = i,
        clicks_context = i,
        date = ts)
    }
    val campaignPerformances = 0 until 60 map
      (i => createCampaignPerformance(plusMinutes(i), i + 1).put)

    //NetAdvisedBidHistory
    val netAdvisedBidHistory = for (i <- 0 until 60; bp <- bannerPhrases)
      yield NetAdvisedBidHistory(
      bannerphrase_id = bp.id, //fk
      date = plusMinutes(i),
      a = i,
      b = i + 1,
      c = i + 3,
      d = i + 4,
      e = 0,
      f = 0).put

    //ActualBidHistory
    val actualBidHistory = for (i <- 0 until 60; bp <- bannerPhrases)
      yield ActualBidHistory(
      bp.id,
      plusMinutes(i),
      bid = i + 3).put

  }
}