package fr.dailybrain.akka.blackjack.actors
import akka.actor.{Actor, Props}

import fr.dailybrain.akka.blackjack.actors.Messages._
import fr.dailybrain.akka.blackjack.models.Deck.new52
import fr.dailybrain.akka.blackjack.models.{Card, CutCard}
import scala.collection.immutable.Seq

import scala.util.Random.shuffle

object Shoe {

  def props(numberOfDecks: Int, cutCardPosition: Int): Props =
    Props(new Shoe(numberOfDecks, cutCardPosition))

}

class Shoe(numberOfDecks: Int, cutCardPosition: Int) extends Actor {

  override def preStart: Unit = self ! Shuffle

  def receive: Receive = active(Seq.empty)

  def active(cards: Seq[Card]): Receive = {

    case Take =>
      val card = cards.head
      val rest = cards.drop(1)
      sender ! card
      context become active(rest)

    case Shuffle =>
      val decks = shuffle((1 to numberOfDecks).flatMap(_ => new52()))
      val (front, back) = decks.splitAt(cutCardPosition)
      val newShoe = front ++ Seq(CutCard) ++ back
      context become active(newShoe)

  }

}