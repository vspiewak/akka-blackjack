package fr.dailybrain.akka.blackjack.strategies

import fr.dailybrain.akka.blackjack.models._
import fr.dailybrain.akka.blackjack.models.Implicits._
import scala.collection.immutable.Seq

object BasicStrategy {

  def surrender(playerCards: Seq[PlayingCard], dealerCard: PlayingCard): Action = {

    (playerCards.kind, dealerCard) match {

      // versus 9
      case (HardHand(16), PlayingCard(`9`, _)) => Surrender

      // versus 10
      case (HardHand(16), PlayingCard(`10`, _)) => Surrender
      case (HardHand(15), PlayingCard(`10`, _)) => Surrender
      case (HardHand(14), PlayingCard(`10`, _)) => Surrender
      //
      case (Pair(8), PlayingCard(`10`, _)) => Surrender
      case (Pair(7), PlayingCard(`10`, _)) => Surrender

      // versus Ace
      case (HardHand(17), PlayingCard(Ace, _)) => Surrender
      case (HardHand(16), PlayingCard(Ace, _)) => Surrender
      case (HardHand(15), PlayingCard(Ace, _)) => Surrender
      case (HardHand(14), PlayingCard(Ace, _)) => Surrender
      case (HardHand(13), PlayingCard(Ace, _)) => Surrender
      case (HardHand(12), PlayingCard(Ace, _)) => Surrender
      case (HardHand(7), PlayingCard(Ace, _)) => Surrender
      case (HardHand(6), PlayingCard(Ace, _)) => Surrender
      case (HardHand(5), PlayingCard(Ace, _)) => Surrender
      //
      case (Pair(8), PlayingCard(Ace, _)) => Surrender
      case (Pair(7), PlayingCard(Ace, _)) => Surrender
      case (Pair(6), PlayingCard(Ace, _)) => Surrender
      case (Pair(3), PlayingCard(Ace, _)) => Surrender

      // rest
      case _ => NoSurrender

    }

  }


  def doubleOrNot(playerCards: Seq[PlayingCard], dealerCard: PlayingCard): Option[Action] = {

    (playerCards.kind, dealerCard) match {

      case _ if playerCards.length > 2 => None

      // Hard Hands
      case (HardHand(11), d) if d.value <= 9 => Some(DoubleDown)
      case (HardHand(10), d) if d.value <= 9 => Some(DoubleDown)
      case (HardHand(9), d) if Seq(3, 4, 5, 6) contains d.value => Some(DoubleDown)

      // Pairs
      case (Pair(5), d) if d.value <= 9 => Some(DoubleDown)

      // Soft Hands
      case (SoftHand(13), d) if Seq(5, 6).contains(d.value) => Some(DoubleDown)
      case (SoftHand(14), d) if Seq(5, 6).contains(d.value) => Some(DoubleDown)
      //
      case (SoftHand(15), d) if Seq(4, 5, 6).contains(d.value) => Some(DoubleDown)
      case (SoftHand(16), d) if Seq(4, 5, 6).contains(d.value) => Some(DoubleDown)
      //
      case (SoftHand(17), d) if Seq(3, 4, 5, 6).contains(d.value) => Some(DoubleDown)
      case (SoftHand(18), d) if Seq(3, 4, 5, 6).contains(d.value) => Some(DoubleDown)

      case _ => None

    }

  }


  def splitOrNot(playerCards: Seq[PlayingCard], dealerCard: PlayingCard, canSplit: Boolean = false): Option[Action] = {

    (playerCards.kind, dealerCard) match {

      case _ if !canSplit => None
      case (Pair(11), d) if d.rank != Ace => Some(Split)
      //
      case (Pair(9), d) if !(Seq(7, 10, 11) contains d.value) => Some(Split)
      case (Pair(8), d) if d.value <= 8 => Some(Split)
      case (Pair(6), d) if d.value <= 6 => Some(Split)
      case (Pair(7), d) if d.value <= 7 => Some(Split)
      case (Pair(4), d) if Seq(5, 6) contains d.value => Some(Split)
      case (Pair(3), d) if d.value <= 7 => Some(Split)
      case (Pair(2), d) if d.value <= 7 => Some(Split)
      //
      case _ => None

    }

  }


  def hitOrStand(playerCards: Seq[PlayingCard], dealerCard: PlayingCard): Action = {

    (playerCards.kind, dealerCard) match {

      case (BlackJack, _) => Stand
      //
      case (HardHand(16), d) if d.value == 10 && playerCards.length >= 3 => Stand
      case (HardHand(n), _) if n >= 17 => Stand
      //
      case (HardHand(16), d) if d.value <= 6 => Stand
      case (HardHand(15), d) if d.value <= 6 => Stand
      case (HardHand(14), d) if d.value <= 6 => Stand
      case (HardHand(13), d) if d.value <= 6 => Stand
      case (HardHand(12), d) if Seq(4, 5, 6).contains(d.value) => Stand
      case (HardHand(_), _) => Hit
      //
      case (Pair(11), d) if d.rank != Ace => Stand
      case (Pair(10), _) => Stand
      case (Pair(9), _) => Stand
      case (Pair(8), d) if d.value <= 6 => Stand
      case (Pair(7), d) if d.value <= 6 => Stand
      case (Pair(6), d) if Seq(4, 5, 6).contains(d.value) => Stand
      case (Pair(_), _) => Hit

      // Soft Hands
      case (SoftHand(n), c) if n >= 19 => Stand
      case (SoftHand(18), c) if c.rank == Ace && playerCards.length >= 4 => Stand
      case (SoftHand(18), c) if !(Seq(9, 10, 11) contains c.value) => Stand
      case (SoftHand(_), _) => Hit
    }

  }


  def play(playerCards: Seq[PlayingCard], dealerCard: PlayingCard, canSplit: Boolean = true): Action = {

    splitOrNot(playerCards, dealerCard, canSplit)
      .orElse(doubleOrNot(playerCards, dealerCard))
      .getOrElse(hitOrStand(playerCards, dealerCard))

  }

}
