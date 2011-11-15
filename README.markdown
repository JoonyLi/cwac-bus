CWAC Bus: Services And Activities, Communicating
================================================

**THIS PROJECT IS DISCONTINUED &mdash; USE AT YOUR OWN RISK**

The CWAC Bus module implements an abstract framework, plus
two concrete implementations, of a local message bus. The
idea is that services and activities can communicate via
the bus without the one knowing directly about the other.

The big advantage of using the bus comes with queued messages.
Right now, if you register some sort of listener object
directly with a local service, and that activity is destroyed
as part of a screen rotation, two things might happen:

1. You fail to unregister the listener, and the service
tries using it, causing no results (at best) or a crash
(at worst)
2. You unregister in `onDestroy()` and re-register in
`onCreate()`, and then miss out on important messages that
occurred while you were being flipped about

When you unregister from the bus, though, you can supply
a queue. That queue will gather up messages while your
activity is gone. When you re-register, all those queued
up messages are delivered to you, so you don't miss a thing.

This feature is not always needed and should be used
judiciously. For example, if your activity will be gone
for a rather long time, you might not need all those events.
Consider either supplying a bounded queue or, perhaps, your
own custom queue subclass that can toss out messages that
will not be needed, or something.

Usage
-----
Full instructions for using this module are forthcoming. Stay
tuned!

Dependencies
------------
This project requires the [CWAC Task module][task]. A copy of
a compatible JAR can be found in the `libs/` directory of
the project, though you are welcome to try a newer one, or
one that you have patched yourself.

Version
-------
This is version 0.1 of this module, meaning it is pretty darn
new.

Demo
----
In the `com.commonsware.cwac.bus.demo` package you will find
a sample activity that demonstrates the use of `SimpleBus`,
a bus that uses `Bundle`s as the messages and arbitrary `String`s
to identify who all is interested in messages. There is also
an `IntentBus` implementation that uses `Intent`s as messages
and `IntentFilter`s to determine who gets what message, but
that is somewhat more cumbersome to use.

Note that when you build the JAR via `ant jar`, the sample
activity is not included, nor any resources -- only the
compiled classes for the actual library are put into the JAR.

License
-------
The code in this project is licensed under the Apache
Software License 2.0, per the terms of the included LICENSE
file.

Questions
---------
**THIS PROJECT IS UNSUPPORTED**

[task]: http://github.com/commonsguy/cwac-task/tree/master
