package fr.dailybrain.akka.blackjack.models

case class Situation(playerCards: Seq[PlayingCard], dealerCard: PlayingCard, canSplit: Boolean = true) {

}
