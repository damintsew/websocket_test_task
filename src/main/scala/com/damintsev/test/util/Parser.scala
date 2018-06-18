package com.damintsev.test.util

import com.damintsev.test.domain.Event
import com.damintsev.test.domain.EventType._

import scala.util.parsing.combinator.RegexParsers


class Parser extends RegexParsers {

  def parse(data: String): Event = {

    parseAll(event, data) match {
      case Success(event, _)  => event
      case error: NoSuccess => throw new IllegalArgumentException("unknown payload")
    }
  }
  private def event = follow | unfollow | broadcast | privateMessage | statusEvent

  private def longParser = """(\d+)""".r ^^ { _.toLong }
  
  private def follow: Parser[Event] = longParser ~ s"|${FOLLOW.flag}|" ~ longParser  ~ "|" ~ longParser  ^^ {
    case sequence ~ _ ~ userFrom ~ _ ~ userTo => Event(sequence, FOLLOW, userFrom, userTo)
  }

  private def unfollow: Parser[Event] = longParser~ s"|${UNFOLLOW.flag}|" ~ longParser  ~ "|" ~ longParser  ^^ {
    case sequence ~ _ ~ userFrom ~ _ ~ userTo => Event(sequence, UNFOLLOW, userFrom, userTo)
  }

  private def broadcast: Parser[Event] = longParser ~ s"|${BROADCAST.flag}" ^^ {
    case sequence ~ _ => new Event(sequence, BROADCAST)
  }

  private def privateMessage: Parser[Event] = longParser ~ s"|${PRIVATE_MESSAGE.flag}|" ~ longParser  ~ "|" ~ longParser  ^^ {
    case sequence ~ _ ~ userFrom ~ _ ~ userTo => Event(sequence, PRIVATE_MESSAGE, userFrom, userTo)
  }

  private def statusEvent: Parser[Event] = longParser ~ s"|${STATUS_EVENT.flag}|" ~ longParser  ^^ {
    case sequence ~ _ ~ userFrom => new Event(sequence, STATUS_EVENT, userFrom)
  }
}

