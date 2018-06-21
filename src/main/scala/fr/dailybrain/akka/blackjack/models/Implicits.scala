package fr.dailybrain.akka.blackjack.models

import scala.collection.immutable.Seq

object Implicits {

  implicit class SeqOfPlayingCardImprovements(cards: Seq[PlayingCard]) {

    def kind: HandKind = {

      cards match {

        // BlackJack
        case Seq(c1, c2) if c1.value + c2.value == 21 => BlackJack

        // Pair
        case Seq(c1, c2) if c1.value == c2.value => Pair(c1.value)

        // Hard Hand (no ace)
        case _ if !cards.exists(_.rank == Ace) =>
          val sumOfValues = cards.map { _.value }.sum
          HardHand(sumOfValues)

        // Soft or Hard Hand
        case _ =>

          val sumOfValuesWithoutAces = cards.filter { _.rank != Ace }.map { _.value }.sum
          val nbAces = cards.count { _.rank == Ace }
          val aceValues = cards.filter { _.rank == Ace }.flatMap { _ => Seq(1, 11) }

          val aceValuesCombinations = aceValues.combinations(nbAces)

          val nonBusted = aceValuesCombinations.map { _ :+ sumOfValuesWithoutAces }.filter { _.sum <= 21 }
          val softHands = nonBusted.filter { _.contains(11) }.map { _.sum }
          val hardHands = nonBusted.filter { !_.contains(11) }.map { _.sum }

          if (softHands.nonEmpty)
            SoftHand(softHands.max)

          else if (hardHands.nonEmpty)
            HardHand(hardHands.max)

          else
            HardHand(sumOfValuesWithoutAces + nbAces)

      }

    }

  }

}
