package dao.squerylorm.test.helpers

import dao.squerylorm._

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
    //create user
    val user = User("krisp0", "123").put

    //create campaigns
    val campaign = user.campaignsRel.associate(
      Campaign(user.id, name = "Campaign_0", _login = "krisp0"))

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
        region_id = region.id).put
  }
}