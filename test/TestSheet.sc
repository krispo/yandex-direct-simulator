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
                                                  //| h3.random.RandomDataImpl@3003ad53

  1 to 5 map (i => r.nextBinomial(10, 0.3))       //> res4: scala.collection.immutable.IndexedSeq[Int] = Vector(2, 5, 5, 4, 1)

  val list: List[Int] = List() //List(3, 2, 4, 1) //> list  : List[Int] = List()
  list.sum                                        //> res5: Int = 0

  val st = "qwerty,we.23..sd;[-=340Pw'df df -- fsdf-----we    kjdgdf"
                                                  //> st  : String = qwerty,we.23..sd;[-=340Pw'df df -- fsdf-----we    kjdgdf
  if ("qwerty,we.23..sd;[-=340Pw'df df -- fsdf-----we    kjdgdf" == st) 1 else 0
                                                  //> res6: Int = 1

}