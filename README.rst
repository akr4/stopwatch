StopWatch
============
StopWatch to measure elapsed times in codes

Getting Started
----------------
example::

    val s = StopWatch.newOne
    s.doWith("task1") {
      // do something
    }

In thie case, EventLogger is used as the default StopWatchEventListener,
and it put a log entry through SLF4J "stopwatch" category.

You can start and stop explicitly::

    val s = StopWatch.newOne
    s.start("task1")
    s.stop()

Note
--------
- not thread-safe

License
---------
Copyright 2012 Akira Ueda

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

