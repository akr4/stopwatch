StopWatch
============
StopWatch to measure elapsed times in Scala codes.

Getting Started
----------------
add the repository and the dependency to your build.sbt::

.. code-block: scala

    resolvers ++= Seq(
      "akr4.github.com releases" at "http://akr4.github.com/mvn-repo/releases"
    )

    libraryDependencies ++= Seq(
      "net.physalis" %% "stopwatch" % "1.0"
    )

How To Use
--------------
example::

.. code-block: scala

    import net.physalis.stopwatch._
    
    val s = StopWatch.newOne
    s.doWith("task1") {
      // do something
    }

In this case, EventLogger is used as the default StopWatchEventListener implementation,
and it puts a log entry through SLF4J "stopwatch" category like this::

    20:13:10.010 [run-main] INFO  stopwatch - 1888ffbd-d129-4425-8dfd-e9b1138d984d  task1 1054

There are two ways to choose other StopWatchEventListener.

a. pass it explicitly::

.. code-block: scala

    val s = StopWatch.newOne(new MyEventListener)

b. use implicit parameter::

.. code-block: scala

    implicit val listener = new MyEventListener
    
    val s = StopWatch.newOne

You can start and stop explicitly::

.. code-block: scala

    val s = StopWatch.newOne
    s.start("task1")
    s.stop()

Extra Information
-------------------------
stop() method takes extra information as its arguments. It is useful to record the prameters or the result of the process, and so forth::

.. code-block: scala

    val s = StopWatch.newOne
    s.start("task1")
    allCatch either { // process } match {
      case Right(x) => s.stop("SUCCESS", x)
      case Left(t) => s.stop("FAILURE", t.getMessage)
    }

StopWatchEventListener
-------------------------
StopWatchEventListener is an instance to get callback from StopWatch at events. You can customize StopWatch behavior by using your own StopWatchEventListener.

StopWatchEventListner::

.. code-block: scala

    trait StopWatchEventListener {
      def onStop(id: String, section: Section, info: Any*)
    }

Examples
~~~~~~~~~~~~~~~
Write to DB::

.. code-block: scala

    class DbLogger extends StopWatchEventListener {
      def onStop(id: String, section: Section, info: Any*) {
        db.insert(id, section, info:_*)
      }
    }

Stores statistics in memory::

.. code-block: scala

    class StatisticsEventListener extends StopWatchEventListener {
      def onStop(id: String, section: Section, info: Any*) {
        updateStatistics(id, section, info:_*)
      }
    }

Note
--------
- not thread-safe

License
---------
Copyright 2012 Akira Ueda

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

