object TestSheet {
  val a = List((6, 1), (6, 1), (4, 4), (4, 5), (4, 5))
                                                  //> a  : List[(Int, Int)] = List((6,1), (6,1), (4,4), (4,5), (4,5))
  a.distinct                                      //> res0: List[(Int, Int)] = List((6,1), (4,4), (4,5))
  a.removeDuplicates                              //> res1: List[(Int, Int)] = List((6,1), (4,4), (4,5))
  a                                               //> res2: List[(Int, Int)] = List((6,1), (6,1), (4,4), (4,5), (4,5))

  val h = List(true,true,true)                    //> h  : List[Boolean] = List(true, true, true)
  h.find(!_).isDefined                            //> res3: Boolean = false

}