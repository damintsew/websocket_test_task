package com.damintsev.test

import com.damintsev.test.processor.{ClientHandler, EventProcessor}
import com.damintsev.test.socket.{ClientSocketHandler, EventSocketHandler}
import com.damintsev.test.util.Parser
import com.soundcloud.followermaze.Server.{EVENT_SOURCE_PORT, LOGGER, USER_CLIENT_PORT, start}

import scala.util.{Success, Try}

object EntryPoint {

  def main(args: Array[String]): Unit = {

    val clientHandler = new ClientHandler
    val eventProcessor = new EventProcessor(new Parser, clientHandler)

    new Thread(eventProcessor).start()
    new Thread(new EventSocketHandler(eventProcessor = eventProcessor)).start()
    new Thread(new ClientSocketHandler(clientHandler = clientHandler)).start()

//    start(userClientPort, eventSourcePort)
//    LOGGER.info("Ctrl-C for graceful shutdown");
  }
}
