package com.damintsev.test.socket

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.net.{ServerSocket, Socket}

import org.slf4j.LoggerFactory.getLogger

abstract class SocketHandler extends Runnable {

  protected val LOGGER = getLogger(this.getClass)

  protected def createBuffer(socket: Socket): BufferedReader = {
    new BufferedReader(new InputStreamReader(socket.getInputStream))
  }

  protected def initSocketPort(port: Int): ServerSocket = {
    val serverSocket = new ServerSocket(port)
    serverSocket.setReuseAddress(true)
    closeOnExit(serverSocket)

    serverSocket
  }

  private def closeOnExit(serverSocket: ServerSocket): Unit = {
    Runtime.getRuntime.addShutdownHook(new Thread() {

      override def run() {
        try {
          if (!serverSocket.isClosed) {
            serverSocket.close()
          }
        } catch {
          case e: IOException => LOGGER.error("error closing ServerSocket")
        }
      }
    })
  }
}
