package fr.dailybrain.akka.blackjack.models

sealed abstract class HandKind(n: Int) { def value: Int = n }
//
case class SoftHand(n: Int) extends HandKind(n)
case class HardHand(n: Int) extends HandKind(n)
case class Pair(n: Int) extends HandKind(n) { override def value = if(n == 11) 12 else n * 2 }
case object BlackJack extends HandKind(21)
