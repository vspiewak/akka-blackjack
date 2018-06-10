package fr.dailybrain.akka.blackjack.actors

import fr.dailybrain.akka.blackjack.models.{Action, Situation}

object Messages {

  sealed trait Message

  // Shoe
  case object Take extends Message
  case object Shuffle extends Message

  // Player
  case object Play extends Message
  case class PlaceBet(amount: Double) extends Message
  case class TakeBet(amount: Double) extends Message
  case class GiveBet(amount: Double) extends Message
  case class AskSurrender(s: Situation) extends Message
  case class AskPlay(s: Situation) extends Message
  case class DoAction(a: Action) extends Message

  // dealer
  case object Card extends Message
  case object Bust extends Message
  case object NextHand extends Message
  case object DealerPlay extends Message
  case object DealerHit extends Message
  case object PayHands extends Message

}
