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
      case (HardHand(v), c) if (Seq(14, 15, 16) contains v) && c.value == 10 => Surrender
      case (Pair(v), c) if (Seq(7, 8) contains v) && c.value == 10 => Surrender

      // versus Ace
      case (HardHand(v), PlayingCard(Ace, _)) if Seq(5, 6, 7, 12, 13, 14, 15, 16, 17) contains v => Surrender
      case (Pair(v), PlayingCard(Ace, _)) if Seq(3, 6, 7, 8) contains v => Surrender

      // rest
      case _ => NoSurrender

    }

  }


  def doubleOrNot(playerCards: Seq[PlayingCard], dealerCard: PlayingCard): Option[Action] = {

    (playerCards.kind, dealerCard.value) match {

      case _ if playerCards.length > 2 => None

      // Hard Hands
      case (HardHand(11), d) if d <= 9 => Some(DoubleDown)
      case (HardHand(10), d) if d  <= 9 => Some(DoubleDown)
      case (HardHand(9), d) if Seq(3, 4, 5, 6) contains d => Some(DoubleDown)

      // Pairs
      case (Pair(5), d) if d <= 9 => Some(DoubleDown)

      // Soft Hands
      case (SoftHand(13), d) if Seq(5, 6) contains d => Some(DoubleDown)
      case (SoftHand(14), d) if Seq(5, 6) contains d => Some(DoubleDown)
      //
      case (SoftHand(15), d) if Seq(4, 5, 6) contains d => Some(DoubleDown)
      case (SoftHand(16), d) if Seq(4, 5, 6) contains d => Some(DoubleDown)
      //
      case (SoftHand(17), d) if Seq(3, 4, 5, 6) contains d => Some(DoubleDown)
      case (SoftHand(18), d) if Seq(3, 4, 5, 6) contains d => Some(DoubleDown)

      case _ => None

    }

  }


  def splitOrNot(playerCards: Seq[PlayingCard], dealerCard: PlayingCard, canSplit: Boolean = true): Option[Action] = {

    (playerCards.kind, dealerCard.value) match {

      case _ if !canSplit => None

      case (Pair(11), _) if dealerCard.rank != Ace => Some(Split)

      case (Pair(9), d) if !(Seq(7, 10, 11) contains d) => Some(Split)
      case (Pair(8), d) if d <= 8 => Some(Split)
      case (Pair(6), d) if d <= 6 => Some(Split)
      case (Pair(7), d) if d <= 7 => Some(Split)
      case (Pair(4), d) if Seq(5, 6) contains d => Some(Split)
      case (Pair(3), d) if d <= 7 => Some(Split)
      case (Pair(2), d) if d <= 7 => Some(Split)

      case _ => None

    }

  }


  def hitOrStand(playerCards: Seq[PlayingCard], dealerCard: PlayingCard): Action = {

    (playerCards.kind, dealerCard.value) match {

      case (BlackJack, _) => Stand

      // Hard Hands
      case (HardHand(v), _) if v >= 17 => Stand
      case (HardHand(16), d) if d == 10 && playerCards.length >= 3 => Stand
      case (HardHand(16), d) if d <= 6 => Stand
      case (HardHand(15), d) if d <= 6 => Stand
      case (HardHand(14), d) if d <= 6 => Stand
      case (HardHand(13), d) if d <= 6 => Stand
      case (HardHand(12), d) if Seq(4, 5, 6) contains d => Stand
      case (HardHand(_), _) => Hit

      // Pairs
      case (Pair(11), _) if dealerCard.rank != Ace => Stand
      case (Pair(10), _) => Stand
      case (Pair(9), _) => Stand
      case (Pair(8), d) if d <= 6 => Stand
      case (Pair(7), d) if d <= 6 => Stand
      case (Pair(6), d) if Seq(4, 5, 6) contains d => Stand
      case (Pair(_), _) => Hit

      // Soft Hands
      case (SoftHand(v), _) if v >= 19 => Stand
      case (SoftHand(18), _) if dealerCard.rank == Ace && playerCards.length >= 4 => Stand
      case (SoftHand(18), d) if !(Seq(9, 10, 11) contains d) => Stand
      case (SoftHand(_), _) => Hit
    }

  }


  def decision(playerCards: Seq[PlayingCard], dealerCard: PlayingCard, canSplit: Boolean = true): Action = {

    splitOrNot(playerCards, dealerCard, canSplit)
      .orElse(doubleOrNot(playerCards, dealerCard))
      .getOrElse(hitOrStand(playerCards, dealerCard))

  }

}
