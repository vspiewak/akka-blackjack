package fr.dailybrain.akka.blackjack.models

case class Hand(bet: Double, cards: List[PlayingCard], splitted: Boolean = false)
