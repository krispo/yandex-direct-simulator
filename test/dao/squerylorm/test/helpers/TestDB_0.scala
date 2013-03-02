package dao.squerylorm.test.helpers

import dao.squerylorm._
import java.sql.Timestamp
import domain.PositionValue

object TestDB_0 extends AppHelpers {
  /*
   * 1 user
   * 1 campaign
   * 1 banner
   * 3 phrases
   * 1 region
   * 3 bannerphrases
   */

  def fill_DB() = {
    val dt_fmt = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd")
    val date = dt_fmt.parseDateTime("2013-01-01")

    //create user
    val user = User("krisp0", "123").put

    //create campaigns
    val campaign = user.campaignsRel.associate(
      Campaign(user.id, name = "Campaign_0", _login = "krisp0"))

    //CampaignPerformance
    val campaignPerformance = CampaignPerformance(campaign_id = campaign.id, date = date).put

    //Banner
    val banner = Banner("Banner_0").put

    //Phrases
    val phrases = 0 until 3 map (i => Phrase("Phrase_" + i.toString).put) toList

    //Regions
    val region = Region(description = "Russia").put

    //BannerPhrase
    val bannerPhrases =
      for (i <- 0 until 3)
        yield BannerPhrase(campaign.id,
        banner_id = banner.id,
        phrase_id = phrases(i).id,
        region_id = region.id,
        //prior is like CTR 
        min = 0.01,
        max = 0.1,
        pMin = 0.15,
        pMax = 0.2,
        delta = 0.1,
        //cumulative traffic during the day
        n = 10000).put

    //BannerPhrasePerformance
    val bannerPhrasePerformance = bannerPhrases map {
      bp => BannerPhrasePerformance(bannerphrase_id = bp.id, date = date).put
    }

    //NetAdvisedBidHistory
    val netAdvisedBidHistory = bannerPhrases map {
      bp => NetAdvisedBidHistory(bannerphrase_id = bp.id, date = date, a = 0.01, b = 2.00, c = 2.50, d = 3.00).put
    }

    //ActualBidHistory
    val actualBidHistory = bannerPhrases map { //bp id's = {1,2,3}
      bp => ActualBidHistory(bannerphrase_id = bp.id, date = date, bid = bp.id).put
    }
  }
}