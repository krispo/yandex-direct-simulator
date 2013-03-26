package dao.squerylorm

import org.joda.time.DateTime

object Charts {

  def getBudget(oc: Option[Campaign]) =
    oc map {
      c => c.budgetHistory.map(bh => (new DateTime(bh.date).getMillis(), bh.budget)).reverse.tail
    } getOrElse (Nil)
}