package fr.dailybrain.akka.blackjack.models

import org.scalatest._
import fr.dailybrain.akka.blackjack.models.Implicits._

class HandKindSpec extends WordSpec with Matchers {


  "A BlackJack hand" when {

    "2 ♥| 5 ♣|" should {

      "be a hard 7" in {

        val c1 = PlayingCard(`2`, ♥)
        val c2 = PlayingCard(`5`, ♣)
        val hand = List(c1, c2)

        hand.kind shouldBe HardHand(7)

      }

    }

    "J ♠| Q ♦|" should {

      "be a pair of 10's" in {

        val c1 = PlayingCard(Jack, ♥)
        val c2 = PlayingCard(Queen, ♣)
        val hand = List(c1, c2)

        hand.kind shouldBe Pair(10)

      }

    }

    "A ♠| 6 ♦|" should {

      "be a soft 17" in {

        val c1 = PlayingCard(Ace, ♠)
        val c2 = PlayingCard(`6`, ♦)
        val hand = List(c1, c2)

        hand.kind shouldBe SoftHand(17)

      }

    }

    "A ♠| 3 ♦| 3 ♣|" should {

      "be a soft 17" in {

        val c1 = PlayingCard(Ace, ♠)
        val c2 = PlayingCard(`3`, ♦)
        val c3 = PlayingCard(`3`, ♣)
        val hand = List(c1, c2, c3)

        hand.kind shouldBe SoftHand(17)

      }

    }

    "A ♠| A ♦| 2 ♣|" should {

      "be a soft 14" in {

        val c1 = PlayingCard(Ace, ♠)
        val c2 = PlayingCard(Ace, ♦)
        val c3 = PlayingCard(`2`, ♣)
        val hand = List(c1, c2, c3)

        hand.kind shouldBe SoftHand(14)

      }

    }

    "A ♠| A ♦| A ♦| 2 ♣|" should {

      "be a soft 15" in {

        val c1 = PlayingCard(Ace, ♠)
        val c2 = PlayingCard(Ace, ♦)
        val c3 = PlayingCard(Ace, ♣)
        val c4 = PlayingCard(`2`, ♣)
        val hand = List(c1, c2, c3, c4)

        hand.kind shouldBe SoftHand(15)

      }

    }

    "A ♠| K ♣| 6 ♦|" should {

      "be a hard 17" in {

        val c1 = PlayingCard(Ace, ♠)
        val c2 = PlayingCard(King, ♣)
        val c3 = PlayingCard(`6`, ♦)
        val hand = List(c1, c2, c3)

        hand.kind shouldBe HardHand(17)

      }

    }

    "5 ♥ | 9 ♠| A ♣| 10 ♦|" should {

      "be a hard 25" in {

        val c1 = PlayingCard(`5`, ♥)
        val c2 = PlayingCard(`9`, ♠)
        val c3 = PlayingCard(Ace, ♣)
        val c4 = PlayingCard(`10`, ♦)
        val hand = List(c1, c2, c3, c4)

        hand.kind shouldBe HardHand(25)

      }

    }

    "K ♠| 5 ♣| 6 ♦|" should {

      "be a hard 21" in {

        val c1 = PlayingCard(King, ♠)
        val c2 = PlayingCard(`5`, ♣)
        val c3 = PlayingCard(`6`, ♦)
        val hand = List(c1, c2, c3)

        hand.kind shouldBe HardHand(21)

      }

    }

    "A ♠| K ♣|" should {

      "be a BlackJack" in {

        val c1 = PlayingCard(Ace, ♠)
        val c2 = PlayingCard(King, ♣)
        val hand = List(c1, c2)

        hand.kind shouldBe BlackJack

      }

    }

  }

}