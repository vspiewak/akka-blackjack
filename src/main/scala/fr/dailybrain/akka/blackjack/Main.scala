package fr.dailybrain.akka.blackjack

import akka.actor.{ActorSystem, Props}
import fr.dailybrain.akka.blackjack.actors.Messages.Play
import fr.dailybrain.akka.blackjack.actors._
import fr.dailybrain.akka.blackjack.models._

object Main {

  def main(args: Array[String]): Unit = {

    val system: ActorSystem = ActorSystem("akkaBlackjack")
    //
    system.actorOf(Shoe.props(6, (5.5 * 52).toInt), name = "shoe")
    system.actorOf(Dealer.props(), name = "dealer")
    val player = system.actorOf(Player.props(1000, 100), name = "player")

    player ! Play

  }

}

