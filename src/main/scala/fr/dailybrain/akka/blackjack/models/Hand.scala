package fr.dailybrain.akka.blackjack.models

case class Hand(bet: Double, cards: Seq[PlayingCard], splitted: Boolean = false)
