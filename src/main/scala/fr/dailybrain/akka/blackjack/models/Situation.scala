package fr.dailybrain.akka.blackjack.models

import scala.collection.immutable.Seq

case class Situation(playerCards: Seq[PlayingCard], dealerCard: PlayingCard, canSplit: Boolean)
