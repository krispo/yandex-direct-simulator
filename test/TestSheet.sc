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
                                                  //| h3.random.RandomDataImpl@462f4ae2

  1 to 100 map (i => r.nextBinomial(10, 0.3))     //> res4: scala.collection.immutable.IndexedSeq[Int] = Vector(2, 5, 1, 3, 1, 4, 
                                                  //| 3, 1, 5, 2, 5, 3, 3, 2, 4, 1, 1, 2, 2, 2, 4, 2, 5, 4, 4, 5, 4, 4, 2, 3, 1, 2
                                                  //| , 2, 4, 2, 2, 2, 3, 4, 3, 4, 3, 2, 3, 1, 2, 3, 3, 3, 3, 5, 4, 3, 2, 3, 3, 5,
                                                  //|  3, 3, 6, 5, 1, 2, 2, 1, 2, 2, 3, 5, 3, 3, 6, 0, 3, 2, 4, 1, 4, 3, 5, 3, 0, 
                                                  //| 1, 5, 2, 3, 3, 2, 3, 4, 2, 2, 2, 1, 2, 5, 1, 2, 3, 1)
  import org.squeryl.PrimitiveTypeMode._
  import dao.squerylorm._
  import play.api.test._
  import play.api.test.Helpers._
  running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
    inTransaction {
      AppSchema.create
      //fill_DB
    }
  }                                               //> java.lang.ExceptionInInitializerError
                                                  //| 	at TestSheet$$anonfun$main$1$$anonfun$apply$mcV$sp$1$$anonfun$apply$mcV$
                                                  //| sp$2.apply$mcV$sp(TestSheet.scala:27)
                                                  //| 	at TestSheet$$anonfun$main$1$$anonfun$apply$mcV$sp$1$$anonfun$apply$mcV$
                                                  //| sp$2.apply(TestSheet.scala:27)
                                                  //| 	at TestSheet$$anonfun$main$1$$anonfun$apply$mcV$sp$1$$anonfun$apply$mcV$
                                                  //| sp$2.apply(TestSheet.scala:27)
                                                  //| 	at org.squeryl.dsl.QueryDsl$class._using(QueryDsl.scala:46)
                                                  //| 	at org.squeryl.dsl.QueryDsl$class._executeTransactionWithin(QueryDsl.sca
                                                  //| la:115)
                                                  //| 	at org.squeryl.dsl.QueryDsl$class.inTransaction(QueryDsl.scala:100)
                                                  //| 	at org.squeryl.PrimitiveTypeMode$.inTransaction(PrimitiveTypeMode.scala:
                                                  //| 40)
                                                  //| 	at TestSheet$$anonfun$main$1$$anonfun$apply$mcV$sp$1.apply$mcV$sp(TestSh
                                                  //| eet.scala:26)
                                                  //| 	at TestSheet$$anonfun$main$1$$anonfun$apply$mcV$sp$1.apply(TestSheet.sca
                                                  //| la:26)
                                                  //| 	at TestSheet$$anonfun$main$1$$anonfun$apply$mcV$sp$1.apply(TestSheet.sca
                                                  //| la:26)
                                                  //| 	at play.
                                                  //| Output exceeds cutoff limit.
}