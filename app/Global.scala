import org.squeryl.adapters.{ H2Adapter, PostgreSqlAdapter, MySQLInnoDBAdapter }
import org.squeryl.internals.DatabaseAdapter
import org.squeryl.{ Session, SessionFactory }
import play.api.db.DB
import play.api.GlobalSettings

import jobs._
import play.api._
import play.api.Play._

import play.api.Application

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    SessionFactory.concreteFactory = app.configuration.getString("db.default.driver") match {
      case Some("org.h2.Driver") => Some(() => getSession(new H2Adapter, app))
      case Some("org.postgresql.Driver") => Some(() => getSession(new PostgreSqlAdapter, app))
      case Some("com.mysql.jdbc.Driver") => Some(() => getSession(new MySQLInnoDBAdapter, app))
      case _ => sys.error("Database driver must be either org.h2.Driver or org.postgresql.Driver or com.mysql.jdbc.Driver")
    }

    /*Logger.info("!!! Application has STARTED...")
    if (true) Scheduler.start*/
  }

  override def onStop(app: Application) {
    /*Logger.info("!!! Application has FINISHED...")
    Scheduler.shutdown*/
  }

  def getSession(adapter: DatabaseAdapter, app: Application) = Session.create(DB.getConnection()(app), adapter)
}