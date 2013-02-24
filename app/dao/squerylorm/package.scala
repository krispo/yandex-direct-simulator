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

}

