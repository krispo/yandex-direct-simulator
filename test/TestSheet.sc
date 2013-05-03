import scala.xml._
import org.apache.commons.math3.random.RandomDataImpl
import play.api.libs.iteratee._

object TestSheet {
  val a = List((6, 1), (6, 1), (4, 4), (4, 5), (4, 5))
                                                  //> a  : List[(Int, Int)] = List((6,1), (6,1), (4,4), (4,5), (4,5))
  a.distinct                                      //> res0: List[(Int, Int)] = List((6,1), (4,4), (4,5))
  a.removeDuplicates                              //> res1: List[(Int, Int)] = List((6,1), (4,4), (4,5))
  a                                               //> res2: List[(Int, Int)] = List((6,1), (6,1), (4,4), (4,5), (4,5))
  val k = a.fold(Enumerator())(Enumerator(_) >>> Enumerator(_))
                                                  //> k  : Object = play.api.libs.iteratee.Enumerator$$anon$2@7239c5c6
  //val o = Enumerator.pushee(onStart, onComplete, onError)(a(0),a(1))

  val j = Enumerator("qwe", "asd", "zxc")         //> j  : play.api.libs.iteratee.Enumerator[String] = play.api.libs.iteratee.Enum
                                                  //| erator$$anon$21@2ee784c0
  import scala.collection.Iterator
  val f = 1 to 5 toIterator                       //> f  : Iterator[Int] = non-empty iterator

  f                                               //> res3: Iterator[Int] = non-empty iterator

  val strList = List("qwe", "sdf", "qwe", "sdf", "asd")
                                                  //> strList  : List[String] = List(qwe, sdf, qwe, sdf, asd)
  strList.distinct                                //> res4: List[String] = List(qwe, sdf, asd)
  
  /*val h = List(true, true, true)
  h.find(!_).isDefined

  val xml = <report></report>

  val g = List(1, 2, 3).map(p => p + 1).reduce(_ + _)

  //import org.apache.commons.math3.random.RandomGenerator
  val r = new RandomDataImpl()

  1 to 5 map (i => r.nextBinomial(10, 0.3))

  val list: List[Int] = List() //List(3, 2, 4, 1)
  list.sum

  val st = "qwerty,we.23..sd;[-=340Pw'df df -- fsdf-----we    kjdgdf"
  if ("qwerty,we.23..sd;[-=340Pw'df df -- fsdf-----we    kjdgdf" == st) 1 else 0

  val li = List(2, 3, 3, 4, 1, 5)
  li.scan(0)(_ + _).tail
  val ld = List(1.1, 3.4, 3.2, 2.1)
  ld.scan(0.0)(_ + _).tail

  val asd = List(2, 5, 3, 2, 67, 32, 14, 7, 8, 5, 4, 2, 3, 5)
  asd.take(5)
  asd.drop(5)
*/
}