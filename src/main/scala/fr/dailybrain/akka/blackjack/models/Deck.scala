package fr.dailybrain.akka.blackjack.models

import scala.collection.immutable.Seq

object Deck {

  def gen52(): Seq[PlayingCard] = {

    for {

      suit <- Seq(♠, ♥, ♣, ♦)

      rank <- Seq(`2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, `10`) ++
           Seq(Jack, Queen, King, Ace)

    } yield PlayingCard(rank, suit)

  }

}