package net.physalis.stopwatch

import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory
import org.scalamock._

class StopWatchSuite extends FunSuite with MockFactory with ProxyMockFactory with VerboseErrors with CallLogging {

  test("work fine with default listener if no implicit one") {
    val s = StopWatch("id")
    s.start("task1")
    s.stop()
  }

  test("start and stop") {
    implicit val logger = mock[StopWatchEventListener]
    logger expects 'onStop where { (id: String, s: Section, info: Seq[Any]) => id == "id" && s.taskName == "task1" }

    val s = StopWatch("id")
    s.start("task1")
    s.stop()
  }

  test("stop without start do nothing") {
    implicit val logger = mock[StopWatchEventListener]

    val s = StopWatch("id")
    s.stop()
  }

  test("start after start just ignore previous task") {
    implicit val logger = mock[StopWatchEventListener]
    logger expects 'onStop where { (id: String, s: Section, info: Seq[Any]) => id == "id" && s.taskName == "task2" }

    val s = StopWatch("id")
    s.start("task1")
    s.start("task2")
    s.stop()
  }

  test("split property end previous task and then start") {
    implicit val logger = mock[StopWatchEventListener]
    logger expects 'onStop where { (id: String, s: Section, info: Seq[Any]) => id == "id" && s.taskName == "task1" }

    val s = StopWatch("id")
    s.start("task1")
    s.split("task2")
  }

  test("new instance with random id") {
    val s1 = StopWatch.newOne
    val s2 = StopWatch.newOne

    assert(s1.id != s2.id)
  }

  test("doWith calls event property and returns the closure return value") {
    implicit val logger = mock[StopWatchEventListener]
    logger expects 'onStop where { (id: String, s: Section, info: Seq[Any]) => id == "id" && s.taskName == "task1" }

    val s = StopWatch("id")
    val x = s.doWith("task1") { 1 }

    assert(x == 1)
  }

  test("stop with extra informations") {
    implicit val logger = mock[StopWatchEventListener]
    logger expects 'onStop where { (id: String, s: Section, info: Seq[Any]) =>
      id == "id" &&
      s.taskName == "task1" &&
      info == Seq("aaa", 111, 'AAA)
    }

    val s = StopWatch("id")
    s.start("task1")
    s.stop("aaa", 111, 'AAA)
  }

}

