package com.damintsev.test

import com.damintsev.test.processor.{ClientHandler, EventProcessor}
import com.damintsev.test.socket.{ClientSocketHandler, EventSocketHandler}
import com.damintsev.test.util.Parser

object EntryPoint {

  def main(args: Array[String]): Unit = {

    val clientHandler = new ClientHandler
    val eventProcessor = new EventProcessor(new Parser, clientHandler)

    new Thread(eventProcessor).start()
    new Thread(new EventSocketHandler(eventProcessor = eventProcessor)).start()
    new Thread(new ClientSocketHandler(clientHandler = clientHandler)).start()
  }
}
