package com.damintsev.test.socket

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ServerSocket, Socket}

import com.damintsev.test.processor.ClientHandler

class ClientSocketHandler(port: Int = 9099, clientHandler: ClientHandler) extends SocketHandler {

  override def run() {

    val serverSocket = initSocketPort(port)

    while (!serverSocket.isClosed) {
      val socket = serverSocket.accept()

      val buffer = createBuffer(socket)
      val userId = buffer.readLine().toLong

      clientHandler.addClient(userId, socket)
    }
  }
}
