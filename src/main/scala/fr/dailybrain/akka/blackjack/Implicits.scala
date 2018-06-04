package fr.dailybrain.akka.blackjack

import akka.util.Timeout
import scala.concurrent.duration._

object Implicits {
  implicit val atMost = 5 seconds
  implicit val timeout = Timeout(atMost)
}
