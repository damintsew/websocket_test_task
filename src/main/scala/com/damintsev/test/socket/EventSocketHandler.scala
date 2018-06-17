package com.damintsev.test.socket

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ServerSocket, Socket}

import com.damintsev.test.processor.EventProcessor
import com.soundcloud.followermaze.event.Event

import scala.util.{Success, Try}

class EventSocketHandler(port: Int = 9090, eventProcessor: EventProcessor) extends Runnable {

  override def run() {
    val serverSocket = initSocketPort(port)
    val socket = serverSocket.accept
    CloseSocketHook.closeOnExit(socket)

    val buffer = createBuffer(socket)

    while (true) {
      //    while(!serverSocket.isClosed) {


      var payload = buffer.readLine

      while (payload != null) {
//        System.out.println(payload)
        eventProcessor.submitIncomingData(payload)

        payload = buffer.readLine
      }

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
