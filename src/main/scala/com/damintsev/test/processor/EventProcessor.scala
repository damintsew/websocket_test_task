package com.damintsev.test.processor

import java.util.concurrent.{ArrayBlockingQueue, PriorityBlockingQueue}
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import com.damintsev.test.util.Parser
import com.soundcloud.followermaze.event.Event

class EventProcessor(parser: Parser, clientHandler: ClientHandler) extends Runnable {

  private val bufferQueue = new PriorityBlockingQueue[Event] //todo rename
  private val lastSentNumber = new AtomicLong

  def submitIncomingPayload(data: String): Unit = {
    val event = parser.parse(data)

    bufferQueue.put(event)
  }

  def run(): Unit = {

    while (true) {
      val topEvent = bufferQueue.take()

      //checking that our message is the next
      if (lastSentNumber.compareAndSet(topEvent.sequence - 1, topEvent.sequence)) {
        //send
//        System.out.println(topEvent.toString)

          clientHandler.send(topEvent)
      } else {
        bufferQueue.put(topEvent)
      }
    }
  }
}
