package fr.dailybrain.akka.blackjack.actors

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}
import akka.event.Logging
import fr.dailybrain.akka.blackjack.actors.Messages._
import fr.dailybrain.akka.blackjack.models.Situation
import fr.dailybrain.akka.blackjack.strategies.BasicStrategy.{decision, surrender}

import scala.io.StdIn._

object Player {
  def props(startingBankroll: Double, bet: Double, maxRounds: Int): Props = Props(new Player(startingBankroll, bet, maxRounds))
}

case class PlayerState(bankroll: Double, rounds: Int)

class Player(startingBankroll: Double, bet: Double, maxRounds: Int) extends Actor with ActorLogging {

  val dealerActor = context.actorSelection("/user/dealer")

  override def preStart: Unit = log.info("round\tbankroll")

  def receive = active(PlayerState(startingBankroll, 0))

  def active(state: PlayerState): Receive = {

    case Play =>

      if (state.rounds > maxRounds) {

        context.system.terminate()

      } else {

        log.info(s"${state.rounds}\t${state.bankroll.toInt}")
        log.debug(s"Bankroll: ${state.bankroll}")

        //log.debug("Press [Enter] to continue")
        //readLine

        val newState = state.copy(bankroll = state.bankroll - bet, rounds = state.rounds + 1)
        context become active(newState)

        dealerActor ! PlaceBet(bet)

      }


    case GiveBet(amout: Double) =>

      log.debug(s"Dealer give: $amout")
      context become active(state.copy(bankroll = state.bankroll + amout))


    case TakeBet(amout: Double) =>

      log.debug(s"Dealer take from player: $amout")
      context become active(state.copy(bankroll = state.bankroll - amout))


    case AskSurrender(s: Situation) =>

      log.debug("Dealer: Surrender ?")
      val action = surrender(s.playerCards, s.dealerCard)
      log.debug(s"Player: $action !")
      dealerActor ! DoAction(action)


    case AskPlay(s: Situation) =>

      log.debug("Dealer: Play ?")
      val action = decision(s.playerCards, s.dealerCard, s.canSplit)
      log.debug(s"Player: $action !")
      dealerActor ! DoAction(action)


    case _ =>

      log.error("Unhandled message")

  }

}
