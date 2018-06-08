package fr.dailybrain.akka.blackjack.actors

import akka.actor.{Actor, Props}
import fr.dailybrain.akka.blackjack.actors.Messages._
import fr.dailybrain.akka.blackjack.Implicits._
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

object Casino {
  def props(): Props = Props(new Casino())
}

class Casino extends Actor {

  val shoeActor = context.actorSelection("/user/shoe")
  val playerActor = context.actorSelection("/user/player")
  val dealerActor = context.actorSelection("/user/dealer")

  override def receive: Receive = {

    case Start =>

      playerActor ! Play


    case Shutdown =>

      for {
        s <- shoeActor.resolveOne()
        p <- playerActor.resolveOne()
        d <- dealerActor.resolveOne()
      } yield {

        context.stop(s)
        context.stop(p)
        context.stop(d)
        //
        Thread.sleep(10000)
        //
        context.system.terminate()

      }


  }

}
