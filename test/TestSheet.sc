import scala.xml._
import org.apache.commons.math3.random.RandomDataImpl

object TestSheet {
  val a = List((6, 1), (6, 1), (4, 4), (4, 5), (4, 5))
                                                  //> a  : List[(Int, Int)] = List((6,1), (6,1), (4,4), (4,5), (4,5))
  a.distinct                                      //> res0: List[(Int, Int)] = List((6,1), (4,4), (4,5))
  a.removeDuplicates                              //> res1: List[(Int, Int)] = List((6,1), (4,4), (4,5))
  a                                               //> res2: List[(Int, Int)] = List((6,1), (6,1), (4,4), (4,5), (4,5))

  val h = List(true, true, true)                  //> h  : List[Boolean] = List(true, true, true)
  h.find(!_).isDefined                            //> res3: Boolean = false

  val xml = <report></report>                     //> xml  : scala.xml.Elem = <report></report>

  val g = List(1, 2, 3).map(p => p + 1).reduce(_ + _)
                                                  //> g  : Int = 9

  //import org.apache.commons.math3.random.RandomGenerator
  val r = new RandomDataImpl()                    //> r  : org.apache.commons.math3.random.RandomDataImpl = org.apache.commons.mat
                                                  //| h3.random.RandomDataImpl@57e87539
  
  1 to 100 map (i=>r.nextBinomial(10, 0.3) )      //> res4: scala.collection.immutable.IndexedSeq[Int] = Vector(3, 2, 1, 4, 1, 3, 
                                                  //| 4, 3, 0, 4, 1, 3, 1, 2, 4, 4, 3, 2, 3, 1, 2, 3, 3, 2, 2, 0, 4, 0, 4, 3, 3, 2
                                                  //| , 3, 5, 1, 5, 5, 2, 4, 2, 2, 2, 3, 2, 2, 0, 2, 1, 4, 5, 3, 3, 3, 2, 3, 1, 3,
                                                  //|  4, 2, 3, 1, 2, 1, 1, 6, 4, 1, 2, 2, 1, 2, 2, 5, 5, 1, 4, 3, 5, 0, 2, 4, 4, 
                                                  //| 5, 3, 5, 3, 2, 4, 5, 6, 3, 0, 3, 3, 4, 4, 3, 2, 1, 6)
}