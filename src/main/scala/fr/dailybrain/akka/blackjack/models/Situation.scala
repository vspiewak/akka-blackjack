package fr.dailybrain.akka.blackjack.models

case class Situation(playerCards: List[PlayingCard], dealerCard: PlayingCard, canSplit: Boolean = true) {

}
