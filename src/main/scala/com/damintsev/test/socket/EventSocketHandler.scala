package com.damintsev.test.socket

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ServerSocket, Socket}

import com.damintsev.test.processor.EventProcessor
import com.soundcloud.followermaze.event.Event

import scala.util.{Success, Try}

class EventSocketHandler(port: Int = 9090, eventProcessor: EventProcessor) extends SocketHandler {

  override def run() {
    val serverSocket = initSocketPort(port)
    val socket = closeOnExit(serverSocket.accept)

    val buffer = createBuffer(socket)

    while (!serverSocket.isClosed) {
      var payload = buffer.readLine

      while (payload != null) {
        eventProcessor.submitIncomingPayload(payload)

        payload = buffer.readLine
      }
    }
  }
}
