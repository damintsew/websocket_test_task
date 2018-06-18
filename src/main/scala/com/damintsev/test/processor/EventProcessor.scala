package com.damintsev.test.processor

import java.util.concurrent.{ArrayBlockingQueue, PriorityBlockingQueue}
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import com.damintsev.test.domain.Event
import com.damintsev.test.util.Parser

class EventProcessor(parser: Parser, clientHandler: ClientHandler) extends Runnable {

  private val bufferQueue = new PriorityBlockingQueue[Event] //todo rename
  private val lastSentNumber = new AtomicLong

  def submitIncomingPayload(data: String): Unit = {
    val event = parser.parse(data)

    bufferQueue.put(event)
  }

  def run(): Unit = {

    while (true) {
      var topEvent = bufferQueue.peek()

      while (topEvent == null) {
        Thread.sleep(100)
        topEvent = bufferQueue.peek()
      }

      //checking that our message is the next
      if (lastSentNumber.compareAndSet(topEvent.sequence - 1, topEvent.sequence)) {
        bufferQueue.poll() //removing from top
        clientHandler.submitEvent(topEvent)
      }
    }
  }
}
