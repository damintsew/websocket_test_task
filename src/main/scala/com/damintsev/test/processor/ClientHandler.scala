package com.damintsev.test.processor

import java.net.Socket

import com.damintsev.test.domain.{Event, EventType}
import com.damintsev.test.repository.UserRepository
import com.damintsev.test.socket.SocketPatching._
import org.slf4j.LoggerFactory.getLogger

import scala.collection.mutable.HashMap

class ClientHandler {

  private val LOGGER = getLogger(this.getClass)

  private val repository = new UserRepository
  private val userSessionMap = new HashMap[Long, Socket]

  def addClient(userId: Long, socket: Socket): Unit = {
    userSessionMap(userId) = socket
    repository.save(userId)
    LOGGER.info("User {} connected", userId)
  }

  def submitEvent(event: Event) = event.eventType match {
    case EventType.BROADCAST => sendBroadcast(event)
    case EventType.PRIVATE_MESSAGE => sendPM(event)
    case EventType.FOLLOW => follow(event)
    case EventType.UNFOLLOW => unfollow(event)
    case EventType.STATUS_EVENT => sendUpdate(event)
  }

  private def sendBroadcast(event: Event): Unit = {
    send(userSessionMap.keySet.toSet, event)
  }

  private def sendPM(event: Event): Unit = {
    send(event.userTo, event)
  }

  private def follow(event: Event): Unit = {
    repository.get(event.userTo).addFollower(event.userFrom)
    send(event.userTo, event)
  }

  def unfollow(event: Event): Unit = {
    repository.get(event.userTo).removeFollower(event.userFrom)
  }

  private def sendUpdate(event: Event): Unit = {
    val usersToNotify = repository.get(event.userFrom).getFollowers.toSet
    send(usersToNotify, event)
  }

  private def send(userId: Long, event: Event): Unit = {
    send(Set(userId), event)
  }

  private def send(users: Set[Long], event: Event): Unit = {
    users.foreach(userId => {
      if (userSessionMap.contains(userId)) {
        userSessionMap(userId).send(event.toString)
        LOGGER.debug("Event sent {} to user {}", event, userId)
      } else {
        LOGGER.debug("User {} disconnected, skipping event {}", userId, event)
      }
    })
  }
}
