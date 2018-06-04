package fr.dailybrain.akka.blackjack.actors
import akka.actor.{Actor, Props}

import fr.dailybrain.akka.blackjack.actors.Messages._
import fr.dailybrain.akka.blackjack.models.Deck.new52
import fr.dailybrain.akka.blackjack.models.{Card, CutCard, PlayingCard}

import scala.util.Random.shuffle

object Shoe {
  def props(numberOfDecks: Int, penetration: Int): Props = Props(new Shoe(numberOfDecks, penetration))
}

class Shoe(numberOfDecks: Int, penetration: Int) extends Actor {

  override def preStart() =
    self ! Shuffle

  def receive = active(Seq.empty)

  def active(cards: Seq[Card]): Receive = {

    case Take =>
      val card = cards.head
      val rest = cards.drop(1)
      sender ! card
      context become active(rest)

    case Shuffle =>
      val decks: Seq[PlayingCard] = shuffle((1 to numberOfDecks).flatMap(_ => new52()))
      val (front, back) = decks.splitAt(penetration)
      val newShoe = front ++ Seq(CutCard) ++ back
      context become active(newShoe)

  }

}