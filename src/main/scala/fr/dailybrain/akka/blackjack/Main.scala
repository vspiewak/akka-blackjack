package fr.dailybrain.akka.blackjack

import akka.actor.ActorSystem
import fr.dailybrain.akka.blackjack.actors.Messages.Start
import fr.dailybrain.akka.blackjack.actors._

object Main {

  def main(args: Array[String]): Unit = {

    val system: ActorSystem = ActorSystem("akkaBlackjack")
    //
    val casino = system.actorOf(Casino.props(), name = "casino")
    system.actorOf(Shoe.props(6, (5.5 * 52).toInt), name = "shoe")
    system.actorOf(Dealer.props(), name = "dealer")
    system.actorOf(Player.props(1000, 100, 150 * 2000), name = "player")
    //
    casino ! Start

  }

}

