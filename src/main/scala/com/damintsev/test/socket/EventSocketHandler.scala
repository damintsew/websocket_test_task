package com.damintsev.test.socket


import com.damintsev.test.processor.EventProcessor


class EventSocketHandler(port: Int = 9090, eventProcessor: EventProcessor) extends SocketHandler {

  override def run() {

    val serverSocket = initSocketPort(port)

    val socket = serverSocket.accept
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
