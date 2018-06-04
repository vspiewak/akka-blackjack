package fr.dailybrain.akka.blackjack.models

sealed trait Action
//
case object Hit extends Action
case object Stand extends Action
case object DoubleDown extends Action
case object Split extends Action
case object Surrender extends Action
case object NoSurrender extends Action
