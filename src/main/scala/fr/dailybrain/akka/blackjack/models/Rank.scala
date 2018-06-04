package fr.dailybrain.akka.blackjack.models

sealed abstract class Rank(n: Int) {
  def value: Int = n
}

case object `2`   extends Rank(2)
case object `3`   extends Rank(3)
case object `4`   extends Rank(4)
case object `5`   extends Rank(5)
case object `6`   extends Rank(6)
case object `7`   extends Rank(7)
case object `8`   extends Rank(8)
case object `9`   extends Rank(9)
//
case object `10`  extends Rank(10)
case object Jack  extends Rank(10) { override def toString = "J" }
case object Queen extends Rank(10) { override def toString = "Q" }
case object King  extends Rank(10) { override def toString = "K" }
//
case object Ace   extends Rank(11) { override def toString = "A" }

