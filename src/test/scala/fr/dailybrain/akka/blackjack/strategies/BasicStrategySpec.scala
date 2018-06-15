package fr.dailybrain.akka.blackjack.strategies

import fr.dailybrain.akka.blackjack.models._
import fr.dailybrain.akka.blackjack.strategies.BasicStrategy._
import org.scalatest._
import scala.collection.immutable.Seq

class BasicStrategySpec extends WordSpec with Matchers {

  "A Basic Strategy" when {


    "With Hard Hands" should {

      "Hit on 12 vs 2|3" in {

        val action1 = decision(Seq(PlayingCard(King, ♥), PlayingCard(`2`, ♥)), PlayingCard(`2`, ♥))
        val action2 = decision(Seq(PlayingCard(King, ♥), PlayingCard(`2`, ♥)), PlayingCard(`3`, ♥))

        action1 shouldBe Hit
        action2 shouldBe Hit

      }

      "Stand on 12 vs 4|5|6" in {

        val action1 = decision(Seq(PlayingCard(King, ♥), PlayingCard(`2`, ♥)), PlayingCard(`4`, ♥))
        val action2 = decision(Seq(PlayingCard(King, ♥), PlayingCard(`2`, ♥)), PlayingCard(`5`, ♥))
        val action3 = decision(Seq(PlayingCard(King, ♥), PlayingCard(`2`, ♥)), PlayingCard(`6`, ♥))

        action1 shouldBe Stand
        action2 shouldBe Stand
        action3 shouldBe Stand

      }

    }


    "With Pairs" should {

      "Split on 4 4 vs 5|6" in {

        val action1 = decision(Seq(PlayingCard(`4`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`5`, ♥))
        val action2 = decision(Seq(PlayingCard(`4`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`6`, ♥))

        action1 shouldBe Split
        action2 shouldBe Split

      }

      "DoubleDown on 5 5 vs 2|3|4|5|6|7|8|9" in {

        val actions =
          //
          Seq(`2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`)
            //
            .map { v => decision(Seq(PlayingCard(`5`, ♥), PlayingCard(`5`, ♥)), PlayingCard(v, ♥)) }

        actions should contain only DoubleDown

      }

      "Hit on 4 4 vs 5|6 if can't split" in {

        val action1 = decision(Seq(PlayingCard(`4`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`5`, ♥), false)
        val action2 = decision(Seq(PlayingCard(`4`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`6`, ♥), false)

        action1 shouldBe Hit
        action2 shouldBe Hit

      }

      "Stand on 6 6 vs 4|5|6 if can't split" in {

        val action1 = decision(Seq(PlayingCard(`6`, ♥), PlayingCard(`6`, ♥)), PlayingCard(`4`, ♥), false)
        val action2 = decision(Seq(PlayingCard(`6`, ♥), PlayingCard(`6`, ♥)), PlayingCard(`5`, ♥), false)
        val action3 = decision(Seq(PlayingCard(`6`, ♥), PlayingCard(`6`, ♥)), PlayingCard(`6`, ♥), false)

        action1 shouldBe Stand
        action2 shouldBe Stand
        action3 shouldBe Stand

      }

    }


    "With Soft Hands" should {

      "DoubleDown on Soft 18 vs 3|4|5|6" in {

        val action1 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`7`, ♥)), PlayingCard(`3`, ♥))
        val action2 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`7`, ♥)), PlayingCard(`3`, ♥))
        val action3 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`7`, ♥)), PlayingCard(`3`, ♥))
        val action4 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`7`, ♥)), PlayingCard(`3`, ♥))

        action1 shouldBe DoubleDown
        action2 shouldBe DoubleDown
        action3 shouldBe DoubleDown
        action4 shouldBe DoubleDown

      }

      "Stand on Soft 18 (in 3 cards) vs 3|4|5|6 " in {

        val action1 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`3`, ♥))
        val action2 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`3`, ♥))
        val action3 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`3`, ♥))
        val action4 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`4`, ♥)), PlayingCard(`3`, ♥))

        action1 shouldBe Stand
        action2 shouldBe Stand
        action3 shouldBe Stand
        action4 shouldBe Stand

      }

      "Hit on Soft 17 (in 3 cards) vs 3|4|5|6 " in {

        val action1 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`3`, ♥)), PlayingCard(`3`, ♥))
        val action2 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`3`, ♥)), PlayingCard(`3`, ♥))
        val action3 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`3`, ♥)), PlayingCard(`3`, ♥))
        val action4 = decision(Seq(PlayingCard(Ace, ♥), PlayingCard(`3`, ♥), PlayingCard(`3`, ♥)), PlayingCard(`3`, ♥))

        action1 shouldBe Hit
        action2 shouldBe Hit
        action3 shouldBe Hit
        action4 shouldBe Hit

      }

    }

  }

}