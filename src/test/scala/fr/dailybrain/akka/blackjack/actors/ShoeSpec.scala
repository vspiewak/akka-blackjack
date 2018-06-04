package fr.dailybrain.akka.blackjack.actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import fr.dailybrain.akka.blackjack.Implicits._
import fr.dailybrain.akka.blackjack.actors.Messages.{Shuffle, Take}
import fr.dailybrain.akka.blackjack.models.{Card, CutCard, PlayingCard}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global

class ShoeSpec() extends TestKit(ActorSystem("ShoeSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A Shoe actor" must {

    "send a card" in {

      val shoe = system.actorOf(Shoe.props(6, (5.5 * 52).toInt))
      shoe ! Shuffle
      shoe ! Take
      expectMsgType[Card]

    }

    "send the cut card" in {

      val shoe = system.actorOf(Shoe.props(1, 3))
      shoe ! Shuffle

      for {

        c1 <- shoe ? Take
        c2 <- shoe ? Take
        c3 <- shoe ? Take
        c4 <- shoe ? Take

      } yield {

        c1 shouldBe PlayingCard
        c2 shouldBe PlayingCard
        c3 shouldBe PlayingCard
        c4 shouldBe CutCard

      }

    }

  }

}