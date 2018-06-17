package com.damintsev.test.socket

import java.io.IOException
import java.net.Socket

import org.slf4j.LoggerFactory.getLogger

object CloseSocketHook {

  private val LOGGER = getLogger(this.getClass)

  def closeOnExit(socket: Socket): Socket = {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      override def run() {
        try {
          socket.close()
        } catch {
          case e: IOException => LOGGER.error("error on close socket")
        }
      }
    })
    socket
  }
}
