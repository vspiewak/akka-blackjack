package fr.dailybrain.akka.blackjack

import akka.actor.ActorSystem
import fr.dailybrain.akka.blackjack.actors.Messages.Play
import fr.dailybrain.akka.blackjack.actors._

object Main {

  def main(args: Array[String]): Unit = {

    val system: ActorSystem = ActorSystem("akkaBlackjack")
    //
    system.actorOf(Shoe.props(6, (5.5 * 52).toInt), name = "shoe")
    system.actorOf(Dealer.props(), name = "dealer")
    val player = system.actorOf(Player.props(1000, 150 * 2000 * 1), name = "player")
    //
    player ! Play

  }

}

