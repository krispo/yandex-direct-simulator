package dao.squerylorm
import org.squeryl.Session
import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl._
import scala.reflect._

object AppSchema extends Schema {

  val users = table[User]("ad_user")
  on(users)(s => declare(
    s.name is (unique)))

  val banners = table[Banner]
  val regions = table[Region]

  val phrases = table[Phrase]
  on(phrases)(ph => declare(
    ph.phrase is (dbType("varchar(512)"))))

  val bannerphrases = table[BannerPhrase]

  val campaignperformance = table[CampaignPerformance]
  on(campaignperformance)(c => declare(
    c.date is (dbType("timestamp"))))

  val bannerphraseperformance = table[BannerPhrasePerformance]
  on(bannerphraseperformance)(c => declare(
    c.date is (dbType("timestamp"))))

  val actualbidhistory = table[ActualBidHistory]

  val netadvisedbidhistory = table[NetAdvisedBidHistory]
  on(netadvisedbidhistory)(c => declare(
    c.date is (dbType("timestamp"))))

  val budgethistory = table[BudgetHistory]
  on(budgethistory)(c => declare(
    c.date is (dbType("timestamp"))))

  //User *-* Network relation
  val campaigns = table[Campaign]
  
  //User *-* Network relation
  val reports = table[Report]
  on(reports)(r => declare(
    r.content is (dbType("varchar(512)"))))

  // User -* Campaign relation
  val userCampaign = oneToManyRelation(users, campaigns).via((u, c) => u.id === c.user_id)
  
  // User -* Campaign relation
  val userReport = oneToManyRelation(users, reports).via((u, r) => u.id === r.user_id)

  // Campaign -* CampaignPerformance relation
  val campaignPerformance = oneToManyRelation(campaigns, campaignperformance).via((c, b) => c.id === b.campaign_id)
  // Campaign -* BudgetHistory relation
  val campaignBudgetHistory = oneToManyRelation(campaigns, budgethistory).via((c, b) => c.id === b.campaign_id)
   // Campaign -* BannerPhrase relation
  val campaignBannerPhrases = oneToManyRelation(campaigns, bannerphrases).via((c, b) => c.id === b.campaign_id)

  // Banner -* BannerPhrase relation
  val bannerBannerPhrases = oneToManyRelation(banners, bannerphrases).via((b, bp) => b.id === bp.banner_id)
  // Phrase -* BannerPhrase relation
  val phraseBannerPhrases = oneToManyRelation(phrases, bannerphrases).via((b, bp) => b.id === bp.phrase_id)
  // Region -* BannerPhrase relation
  val regionBannerPhrases = oneToManyRelation(regions, bannerphrases).via((b, bp) => b.id === bp.region_id)

  // BannerPhrase -* BannerPhrasePerformance relation
  val bannerPhrasePerformance = oneToManyRelation(bannerphrases, bannerphraseperformance).via((c, b) => c.id === b.bannerphrase_id)
  // BannerPhrase -* ActualBidHistory relation
  val bannerPhraseActualBidHistory = oneToManyRelation(bannerphrases, actualbidhistory).via((c, b) => c.id === b.bannerphrase_id)
  // BannerPhrase -* NetAdvisedBidHistory relation
  val bannerPhraseNetAdvisedBidsHistory = oneToManyRelation(bannerphrases, netadvisedbidhistory).via((c, b) => c.id === b.bannerphrase_id)

  override def drop = {
    Session.cleanupResources
    super.drop
  }
}

