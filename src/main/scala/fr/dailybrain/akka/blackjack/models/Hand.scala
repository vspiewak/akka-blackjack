package fr.dailybrain.akka.blackjack.models

import scala.collection.immutable.Seq

case class Hand(bet: Double, cards: Seq[PlayingCard], splitted: Boolean = false)
