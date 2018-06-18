package com.damintsev.test.util

import com.damintsev.test.domain.Event


class Parser {

  def parse(data: String): Event = {
    Event(data)
  }
}
