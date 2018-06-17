package com.damintsev.test

import scala.collection.mutable.Set

class User(userId: Long) {

  private val followers = Set[Long]()

  def getFollowers = followers

  def addFollower(userId: Long) = followers.add(userId)

  def removeFollower(userId: Long) = followers.remove(userId)

}
