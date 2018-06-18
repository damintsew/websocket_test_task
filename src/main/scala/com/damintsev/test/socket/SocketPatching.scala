package com.damintsev.test.socket

import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import java.net.Socket

object SocketPatching {
  implicit class Patching(socket: Socket) {
    
    def send(content: String) {
      val autoFlush = true
      new PrintWriter(socket.getOutputStream, autoFlush).println(content)
    }
  }

}