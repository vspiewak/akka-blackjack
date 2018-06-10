package fr.dailybrain.akka.blackjack.models

import scala.collection.immutable.Seq

object Deck {

  def gen52(): Seq[PlayingCard] = {

    val suits = Seq(♠, ♥, ♣, ♦)
    //
    val ranks = Seq(`2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, `10`) ++
                Seq(Jack, Queen, King, Ace)

    for {
      s <- suits
      r <- ranks
    } yield PlayingCard(r, s)

  }

}