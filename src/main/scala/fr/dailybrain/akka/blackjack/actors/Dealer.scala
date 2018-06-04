package fr.dailybrain.akka.blackjack.actors

import akka.actor.{Actor, Props}
import fr.dailybrain.akka.blackjack.actors.Messages._
import fr.dailybrain.akka.blackjack.models._
import akka.pattern.ask
import akka.pattern.pipe
import fr.dailybrain.akka.blackjack.Implicits._
import fr.dailybrain.akka.blackjack.models.Implicits._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

object Dealer {
  def props(): Props = Props(new Dealer())
}

case class DealerState(currentHandIndex: Int, hands: List[Hand], dealerCards: List[PlayingCard]) {
  def canSplit: Boolean = hands.length < 3
  def isLastHand = hands.isEmpty || currentHandIndex + 1 == hands.length
  def currentHand = hands(currentHandIndex)
}

class Dealer extends Actor {

  var isLastRound = false
  //
  val shoeActor = context.actorSelection("/user/shoe")
  val playerActor = context.actorSelection("/user/player")


  def printState(state: DealerState): Unit = {

    val dealerCards = state.dealerCards

    println()

    println(
      state.hands.foldLeft("") { (acc, _) => acc + "   " } + s"${dealerCards.mkString("|")}|"
    )

    println()

    println(
      state.hands.foldLeft("") { (acc, hg) => acc + s"${hg.cards.mkString("|")}|    " }
    )

    println()

    println(
      state.hands.foldLeft("") { (acc, hg) => acc + s"(${hg.cards.kind.value}/${dealerCards.kind.value}) " }
    )

    println()

  }


  def receive = active(DealerState(0, List.empty[Hand], List.empty[PlayingCard]))


  def active(state: DealerState): Receive = {

    case Card =>

      val s = sender

      for {
        card <- shoeActor ? Take
      } yield {

        card match {
          case _: PlayingCard => s ! card
          case CutCard =>
            println("CutCard out of the shoe !")
            isLastRound = true
            (shoeActor ? Take) pipeTo s
        }

      }


    case PlaceBet(amount: Double) =>

      println(s"player bet: $amount")

      for {

        playerCardOne <- (self ? Card).mapTo[PlayingCard]
        dealerCard <- (self ? Card).mapTo[PlayingCard]
        playerCardTwo <- (self ? Card).mapTo[PlayingCard]

      } yield {

        val playerCards = List(playerCardOne, playerCardTwo)
        val hand = Hand(amount, playerCards)
        //val newState = state.copy(currentHandIndex = 0, hands = List(hand), dealerCards = List(dealerCard))
        val newState = DealerState(0, List(hand), List(dealerCard))

        printState(newState)

        context become active(newState)

        playerActor ! AskSurrender(Situation(playerCards, dealerCard))

      }


    case DoAction(Surrender) =>

      val refund = state.currentHand.bet / 2
      println(s"dealer: refund to player: $refund")
      playerActor ! GiveBet(refund)

      val newHands: List[Hand] = state.hands.patch(state.currentHandIndex, Nil, 1)
      val newHandIndex = if(state.currentHandIndex == 0) 0 else state.currentHandIndex - 1
      val newState: DealerState = state.copy(currentHandIndex = newHandIndex, hands = newHands)

      printState(newState)

      context become active(newState)

      self ! NextHand



    case DoAction(NoSurrender) =>

      println("DEBUG !!!")
      printState(state)
      println("DEBUG !!!")

      playerActor ! AskPlay(
        Situation(
          state.currentHand.cards,
          state.dealerCards.head))


    case DoAction(Stand) =>
      self ! NextHand


    case DoAction(Hit) =>

      for {
        card <- (self ? Card).mapTo[PlayingCard]
      } yield {

        println(s"Dealer took for player: $card")

        val newPlayerCards: List[PlayingCard] = state.currentHand.cards :+ card
        val newHand: Hand = state.currentHand.copy(cards = newPlayerCards)
        val newHands: List[Hand] = state.hands.patch(state.currentHandIndex, List(newHand), 1)
        val newState: DealerState = state.copy(hands = newHands)

        printState(newState)

        context become active(newState)

        newState.currentHand.cards.kind match {
          case h if h.value > 21 =>
            self ! Bust
          case _ =>
            playerActor ! AskPlay(Situation(newState.currentHand.cards, newState.dealerCards.head, newState.canSplit))
        }

      }


    case DoAction(DoubleDown) =>

      playerActor ! TakeBet(state.currentHand.bet)

      for {
        card <- (self ? Card).mapTo[PlayingCard]
      } yield {

        println(s"Dealer took for player: $card")

        val newPlayerCards: List[PlayingCard] = state.currentHand.cards :+ card
        val newHand: Hand = state.currentHand.copy(bet = state.currentHand.bet * 2, cards = newPlayerCards)
        val newHands: List[Hand] = state.hands.patch(state.currentHandIndex, List(newHand), 1)
        val newState: DealerState = state.copy(hands = newHands)

        printState(newState)

        context become active(newState)

        newState.currentHand.cards.kind match {
          case h if h.value > 21 =>
            self ! Bust
          case _ =>
            self ! NextHand
        }

      }


    case DoAction(Split) =>

      playerActor ! TakeBet(state.currentHand.bet)

      for {

        c3 <- (self ? Card).mapTo[PlayingCard]
        c4 <- (self ? Card).mapTo[PlayingCard]

      } yield {

        println(s"Dealer took for player: $c3| & $c4|")

        val c1 = state.currentHand.cards(0)
        val c2 = state.currentHand.cards(1)

        val hand1 = state.currentHand.copy(splitted = true, cards = List(c1, c3))
        val hand2 = state.currentHand.copy(splitted = true, cards = List(c2, c4))

        val newHands: List[Hand] = state.hands.patch(state.currentHandIndex, List(hand1, hand2), 1)
        val newState: DealerState = state.copy(hands = newHands)

        //fixme: remove println
        if(newState.hands.length > 3)
          println("TOO MUCH SPLIT WTF")

        printState(newState)

        context become active(newState)

        (c1.rank, c2.rank) match {
          case (Ace, Ace) =>
            //fixme: should move to next next hand
            self ! DealerMove
          case _ =>
            playerActor ! AskPlay(Situation(newState.currentHand.cards, newState.dealerCards.head, newState.canSplit))
        }

      }


    case DealerMove =>

      println("Dealer Move now :)")

      val allBusted = state.hands.isEmpty
      val onlyBJ = !allBusted && !state.hands.exists { _.cards.kind != BlackJack }
      val notOnlyBJ = state.hands.count { h => h.splitted || h.cards.kind != BlackJack } > 0

      //fixme: remove corner case with dealer A A
      val shouldHit =
        notOnlyBJ && (state.dealerCards.kind == Pair(11) ||  state.dealerCards.kind.value < 17 ) ||
          onlyBJ && state.dealerCards.size == 1 && state.dealerCards.head.value > 9

      if (shouldHit)
        self ! DealerHit
      else
        self ! DealerPayHands


    case DealerHit =>

      for {
        card <- (self ? Card).mapTo[PlayingCard]
      } yield {

        val newState = state.copy(dealerCards = state.dealerCards :+ card)
        printState(newState)

        context become active(newState)

        self ! DealerMove

      }


    case DealerPayHands =>
      println("DealerPayHands !")

      state.hands.foreach { h =>

        (h.cards.kind, state.dealerCards.kind) match {
          case (BlackJack, t) if !h.splitted && t != BlackJack  =>
            playerActor ! GiveBet(h.bet * 2.5)
          case (p, d) if d.value > 21 || p.value > d.value =>
            playerActor ! GiveBet(h.bet * 2)
          case (p, d) if p.value == d.value =>
            playerActor ! GiveBet(h.bet)
          case _ => ()
        }

      }

      if(isLastRound) {
        println("Reshuffle...")
        isLastRound = false
        shoeActor ! Shuffle
      }

      playerActor ! Play


    case NextHand =>

      printState(state)

      if (!state.isLastHand) {

        val newState: DealerState = state.copy(currentHandIndex = state.currentHandIndex + 1)

        context become active(newState)

        playerActor ! AskPlay(Situation(newState.currentHand.cards, newState.dealerCards.head, newState.canSplit))

      } else {

        self ! DealerMove

      }


    case Bust =>

      println("Dealer: Bust !")

      val newHands: List[Hand] = state.hands.patch(state.currentHandIndex, Nil, 1)
      val newHandIndex = if(state.currentHandIndex == 0) 0 else state.currentHandIndex - 1
      val newState: DealerState = state.copy(currentHandIndex = newHandIndex, hands = newHands)

      printState(newState)

      context become active(newState)

      if (newState.isLastHand)
        self ! DealerMove
      else
        self ! NextHand


    case _ =>
      println("Unhandled message")


  }

}