package fr.dailybrain.akka.blackjack.models

import org.scalatest._

class DeckSpec extends WordSpec with Matchers {

  "A single deck" should {

    "have 52 cards" in {

      val deck = Deck.gen52()

      deck.size shouldBe 52
      deck.count(_.rank == Ace) shouldBe 4
      deck.count(_.suit == ♥) shouldBe 13

    }

  }

}