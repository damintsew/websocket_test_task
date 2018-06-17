package com.damintsev.test.processor

import java.net.Socket

import com.damintsev.test.UserRepository
import com.soundcloud.followermaze.transport.RichSocket._
import com.soundcloud.followermaze.event.{Event, EventType}
import org.slf4j.LoggerFactory.getLogger

import scala.collection.mutable.HashMap

class ClientHandler {

  private val LOGGER = getLogger(this.getClass)
  private val repository = new UserRepository
  //todo move down
  private val userSessionMap = new HashMap[Long, Socket]

  def addClient(userId: Long, socket: Socket): Unit = {
    userSessionMap(userId) = socket
    repository.save(userId)
    LOGGER.info("User {} is connected", userId)
  }


  def send(event: Event) = event.eventType match {

    case EventType.BROADCAST => sendBroadcast(event)
    case EventType.PRIVATE_MESSAGE => sendPM(event)
    case EventType.FOLLOW => follow(event)
    case EventType.UNFOLLOW => unfollow(event)
    case EventType.STATUS_EVENT => sendUpdate(event)
    case _ => System.out.println("fuck")
  }

  def sendBroadcast(event: Event): Unit = {
    send(userSessionMap.keySet.toSet, event)
  }

  def sendPM(event: Event): Unit = {
    send(event.userTo, event)
  }

  def follow(event: Event): Unit = {
    repository.get(event.userTo).addFollower(event.userFrom)
    send(event.userTo, event)
  }

  def unfollow(event: Event): Unit = {
    repository.get(event.userTo).removeFollower(event.userFrom)
  }

  def sendUpdate(event: Event): Unit = {
    val usersToNotify = repository.get(event.userFrom).getFollowers.toSet
    send(usersToNotify, event)
  }

  def send(userId: Long, event: Event): Unit = {
    send(Set(userId), event)
  }

  def send(users: Set[Long], event: Event): Unit = {
    users.foreach(userId => {
      if (hasSessionActive(userId)) {
        userSessionMap(userId).send(event.toString)
        LOGGER.debug("Sent event {} to user {}", event, userId)
      } else {
        LOGGER.debug("User {} disconnected, dropped event {}", userId, event)
      }
    })
  }

  private def hasSessionActive(userId: Long): Boolean = userSessionMap.contains(userId)
}
