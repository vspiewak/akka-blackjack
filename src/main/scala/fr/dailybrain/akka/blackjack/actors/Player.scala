package fr.dailybrain.akka.blackjack.actors

import akka.actor.{Actor, Props}
import fr.dailybrain.akka.blackjack.actors.Messages._
import fr.dailybrain.akka.blackjack.models.Situation
import fr.dailybrain.akka.blackjack.strategies.BasicStrategy.{play, surrender}

import scala.io.StdIn._

object Player {
  def props(startingBankroll: Double, bet: Double): Props = Props(new Player(startingBankroll, bet))
}

case class PlayerState(bankroll: Double, bet: Double, rounds: Int)

class Player(startingBankroll: Double, bet: Double) extends Actor {

  val dealerActor = context.actorSelection("/user/dealer")

  def receive = active(PlayerState(startingBankroll, bet, 0))

  def active(state: PlayerState): Receive = {

    case Play =>

      println(s"Bankroll: ${state.bankroll}")
      println(s"Stats: ${state.rounds},${state.bankroll.toInt}")
      //
      //println("Press [Enter] to continue")
      //readLine

      val newState = state.copy(bankroll = state.bankroll - bet, rounds = state.rounds + 1)
      println(s"PlayerState: $state")

      dealerActor ! PlaceBet(bet)
      context become active(newState)

    case AskSurrender(s: Situation) =>
      println("Dealer: Surrender ?")
      val action = surrender(s.playerCards, s.dealerCard)
      println(s"Player: $action !")
      dealerActor ! DoAction(action)


    case AskPlay(s: Situation) =>
      println("Dealer: Play ?")
      val action = play(s.playerCards, s.dealerCard, s.canSplit)
      println(s"Player: $action !")
      dealerActor ! DoAction(action)


    case GiveBet(amout: Double) =>
      println(s"Dealer give: $amout")
      context become active(state.copy(bankroll = state.bankroll + amout))


    case TakeBet(amout: Double) =>
      println(s"Dealer take from player: $amout")
      context become active(state.copy(bankroll = state.bankroll - amout))

    case _ =>
      println("Unhandled message")

  }

}
