package com.damintsev.test.domain

import scala.collection.mutable.ListBuffer

case class Event(sequence: Long, eventType: EventType, userFrom: Long, userTo: Long) extends Comparable[Event] {

  def this(sequence: Long, eventType: EventType) = this(sequence, eventType,  userFrom= -1,  userTo= -1)

  def this(sequence: Long, eventType: EventType, userFrom: Long) = this(sequence, eventType, userFrom, userTo = -1)

  override def toString: String = {
    val payload = ListBuffer(sequence, eventType.flag)
    if (userFrom != -1) payload += userFrom
    if (userTo != -1) payload += userTo
    payload.mkString("|")
  }

  override def compareTo(o: Event): Int = sequence.compareTo(o.sequence)
}