package fr.dailybrain.akka.blackjack.actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
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

      val shoe = system.actorOf(Shoe.props(6, 3 * 52))
      shoe ! Shuffle
      shoe ! Take
      expectMsgType[Card]

    }

    "send the cut card" in {

      val shoe = system.actorOf(Shoe.props(1, 2))
      shoe ! Shuffle

      val probe = TestProbe()

      shoe.tell(Take, probe.ref)
      probe.expectMsgType[PlayingCard]

      shoe.tell(Take, probe.ref)
      probe.expectMsgType[PlayingCard]

      shoe.tell(Take, probe.ref)
      probe.expectMsg(CutCard)

    }

  }

}