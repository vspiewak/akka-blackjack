package fr.dailybrain.akka.blackjack.models

sealed abstract class Card

case object CutCard extends Card

case class PlayingCard(rank: Rank, suit: Suit) extends Card {
  def value: Int = rank.value
  override def toString: String = s"$rank $suit"
}