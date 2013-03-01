package domain

object Position extends Enumeration {
  type Position = Value
  val _bottom, _min, _max, _pMin /*premiumMin*/ , _pMax /*premiumMax*/ , _delta, _N = Value
}

object PositionValue {
  import Position._
  type PositionValue = Map[Position, Double]
  def apply(min: Double = 0, max: Double = 0, pMin: Double = 0, pMax: Double = 0, delta: Double = 0, N: Long = 0): PositionValue = Map(
    _min -> min,
    _max -> max,
    _pMin -> pMin,
    _pMax -> pMax,
    _delta -> delta, // is designed to describe shows probability q
    _N -> N //coefficient for traffic function - how many requests during the whole day
    )
}