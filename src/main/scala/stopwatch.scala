/*
 * Copyright 2012 Akira Ueda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.physalis.stopwatch

case class Section(taskName: String, startTime: Long)

trait StopWatchEventListener {
  /** called when the stopwatch stop a task */
  def onStop(id: String, section: Section, info: Any*)
}

class EventLogger extends StopWatchEventListener {
  private val logger = grizzled.slf4j.Logger("stopwatch")

  def onStop(id: String, section: Section, info: Any*) {
    val elapsed = System.currentTimeMillis - section.startTime
    logger.info((List(id, section.taskName, elapsed) ++ info).mkString("\t"))
  }
}

/** StopWatch to measure elapsed times in codes
 *
 * {{{
 * example::
 * 
 *     val s = StopWatch.newOne
 *     s.doWith("task1") {
 *       // do something
 *     }
 *
 * In this case, EventLogger is used as the default StopWatchEventListener,
 * and it puts a log entry through SLF4J "stopwatch" category.
 *
 * You can start and stop explicitly::
 *
 *     val s = StopWatch.newOne
 *     s.start("task1")
 *     s.stop()
 *
 * - not thread-safe
 * }}}
 */
class StopWatch private (val id: String, listener: StopWatchEventListener) {

  private var currentSection: Option[Section] = None

  /** start the timer */
  def start(taskName: String) {
    currentSection = Option(Section(taskName, System.currentTimeMillis))
  }

  /** stop and call back to listener
   *
   * @param info extra information for listener
   */
  def stop(info: Any*) {
    currentSection.map { listener.onStop(id, _, info:_*) }
  }

  /** stop and start */
  def split(taskName: String) {
    stop()
    start(taskName)
  }

  /** start and stop around the given f */
  def doWith[A](taskName: String)(f: => A): A = {
    start(taskName)
    try { f } finally { stop() }
  }

}
object StopWatch {
  /** create new StopWatch with unique id */
  def newOne(implicit listener: StopWatchEventListener = defaultListener) =
    new StopWatch(java.util.UUID.randomUUID.toString, listener)

  /** create new StopWatch with the given id */
  def apply(id: String)(implicit listener: StopWatchEventListener = defaultListener) =
    new StopWatch(id, listener)

  private lazy val defaultListener = new EventLogger
}

