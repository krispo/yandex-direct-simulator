package dao

package object squerylorm {

  import org.squeryl._
  import org.squeryl.PrimitiveTypeMode._
  import org.squeryl.dsl._

  import org.joda.time.DateTime
  import java.sql.Timestamp

  implicit def convertFromJdbc(t: Timestamp) = new DateTime(t)
  implicit def convertToJdbc(t: DateTime) = new Timestamp(t.getMillis())

  import org.joda.time._
  val iso_fmt = format.ISODateTimeFormat.dateTime()
  val sdate = iso_fmt.parseDateTime("1000-01-01T12:00:00.000+04:00")
  val edate = iso_fmt.parseDateTime("3000-01-01T12:00:00.000+04:00")

  trait History {
    def date: Timestamp
  }

  /**
   * define traffic functions
   */
  import scala.math._
  val md = org.joda.time.DateTimeConstants.MILLIS_PER_DAY

  def integral(millis: Int, phase: Double = Pi): Double = {
    1.1 * millis - md / Pi * (cos(millis * Pi / md - phase) - cos(phase))
  }
  /* normalize traffic, i.e., in [0,1] */
  def traffic(dt: DateTime): Double = integral(dt.getMillisOfDay()) / integral(md)
  def traffic(m /*milliseconds*/ : Int): Double = integral(m) / integral(md)

  def trafficDiff(dt_prev: DateTime, dt_next: DateTime): Double = {
    val mp = dt_prev.getMillisOfDay()
    val mn = dt_next.getMillisOfDay()

    if (mn < mp)
      traffic(mn) + (1 - traffic(mp))
    else {
      traffic(mn) - traffic(mp)
    }
  }

  //def trafficFunction(dt: DateTime): Double = 1.1 - cos(dt.getMinuteOfDay() * Pi / m - Pi / 8)
}

