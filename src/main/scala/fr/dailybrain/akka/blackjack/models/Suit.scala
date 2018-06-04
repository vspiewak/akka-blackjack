package fr.dailybrain.akka.blackjack.models

sealed abstract class Suit
//
case object ♠ extends Suit //{ override def toString: String = "♠️️" }
case object ♣ extends Suit //{ override def toString: String = "♣️" }
case object ♥ extends Suit //{ override def toString: String = "♥️" }
case object ♦ extends Suit //{ override def toString: String = "♦️" }
