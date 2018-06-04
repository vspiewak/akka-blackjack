package fr.dailybrain.akka.blackjack.models

object Deck {

  def new52(): List[PlayingCard] = {

    val suits = List(♠, ♥, ♣, ♦)
    //
    val ranks = List(`2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, `10`) ++
                List(Jack, Queen, King, Ace)

    for {
      s <- suits
      r <- ranks
    } yield PlayingCard(r, s)

  }

}