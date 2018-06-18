package com.damintsev.test.repository


import com.damintsev.test.User

import scala.collection.mutable

class UserRepository {

  val users = new mutable.HashMap[Long, User]()

  def get(userId: Long): User = users.get(userId) match {
    case Some(user) => user
    case None => save(userId)
  }

  def save(userId: Long): User = {
    val newUser = new User(userId)
    users(userId) = newUser
    newUser
  }
}
