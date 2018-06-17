package com.damintsev.test.socket

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ServerSocket, Socket}

import com.damintsev.test.processor.ClientHandler

class ClientSocketHandler(port: Int = 9099, clientHandler: ClientHandler) extends Runnable {

  override def run() {
    val serverSocket = initSocketPort(port)

    while (!serverSocket.isClosed) {
      val socket = serverSocket.accept()
      CloseSocketHook.closeOnExit(socket)

      val buffer = createBuffer(socket)
      val userId = Integer.parseInt(buffer.readLine())
      System.out.println(userId)



      clientHandler.addClient(userId, socket)
    }
  }

  def createBuffer(socket: Socket): BufferedReader = {
    new BufferedReader(new InputStreamReader(socket.getInputStream))
  }

  def initSocketPort(port: Int): ServerSocket = {
    val serverSocket = new ServerSocket(port)
    serverSocket.setReuseAddress(true)
    serverSocket
  }
}
