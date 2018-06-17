package com.damintsev.test.util

import com.soundcloud.followermaze.event.{Event, EventType}

class Parser {

  def parse(data: String): Event = {
    Event(data)
  }
}
