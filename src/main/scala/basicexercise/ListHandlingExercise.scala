package basicexercise


object ListHandlingExercise extends App {

  val powersOfTwoClean = List[Int](8, 32, 2, 16384, 512)
  // if (x + 1) of above lists is a multiple of 3
  val (multipleOfThree, notMultipleOfThree) = (powersOfTwoClean.filter(x => (x + 1) % 3 == 0), powersOfTwoClean.filter(x => (x + 1) % 3 != 0))
  val (multipleOfThree_, notMultipleOfThree_) = powersOfTwoClean partition (x => (x + 1) % 3 == 0)
  val powersOfTwo = List[Int](8, 32, 2, 3, 16384, 512, 9)

  /*TESTING*/
  println(multipleOfThree)
  println(notMultipleOfThree)
  println(multipleOfThree_)
  println(notMultipleOfThree_)

  // example - 8  is 2^3 so the power value is 3. If a number is not a power of 2 print a message and discard it
  val powerValues = powersOfTwo.map(x => {
    if (!isPowerOf2(x))
      println(s"Discarding $x")
    x
  }).filter(x => isPowerOf2(x))
  val ascendingSortedPowerValues = powerValues.sorted

  /*TESTING*/
  println(powerValues)
  val ascendingSortedPowerValues_ = powerValues.sortWith((x, y) => x < y)
  // take 2nd element to n-2nd element and sum the values and print it
  val slicedListSum: Int = ascendingSortedPowerValues.slice(1, ascendingSortedPowerValues.length - 1).sum

  /*TESTING*/
  println(ascendingSortedPowerValues)
  println(ascendingSortedPowerValues_)

  def isPowerOf2(num: Int) = {
    num != 0 && ((num & (num - 1)) == 0)
  }

  /*TESTING*/
  println(slicedListSum)
}

