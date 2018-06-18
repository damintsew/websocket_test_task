package com.damintsev.test

import scala.collection.mutable
import scala.collection.mutable.Set

class User(userId: Long) {

  private val followers = Set[Long]()

  def getFollowers: mutable.Set[Long] = followers

  def addFollower(userId: Long): Boolean = followers.add(userId)

  def removeFollower(userId: Long): Boolean = followers.remove(userId)
}
